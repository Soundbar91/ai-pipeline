package io.github.soundbar91.aipipeline.domain.auth.service;

import io.github.soundbar91.aipipeline.domain.auth.dto.request.SignupCompleteRequest;
import io.github.soundbar91.aipipeline.domain.auth.dto.request.SocialLoginRequest;
import io.github.soundbar91.aipipeline.domain.auth.dto.request.TermAgreementRequest;
import io.github.soundbar91.aipipeline.domain.auth.dto.request.TokenRefreshRequest;
import io.github.soundbar91.aipipeline.domain.auth.dto.response.SignupCompleteResponse;
import io.github.soundbar91.aipipeline.domain.auth.dto.response.SocialLoginResponse;
import io.github.soundbar91.aipipeline.domain.auth.dto.response.TermAgreementResponse;
import io.github.soundbar91.aipipeline.domain.auth.dto.response.TokenRefreshResponse;
import io.github.soundbar91.aipipeline.domain.auth.entity.RefreshToken;
import io.github.soundbar91.aipipeline.domain.auth.repository.RefreshTokenRepository;
import io.github.soundbar91.aipipeline.domain.auth.service.social.SocialAuthService;
import io.github.soundbar91.aipipeline.domain.auth.service.social.SocialUserInfo;
import io.github.soundbar91.aipipeline.domain.school.entity.School;
import io.github.soundbar91.aipipeline.domain.school.repository.SchoolRepository;
import io.github.soundbar91.aipipeline.domain.term.entity.Term;
import io.github.soundbar91.aipipeline.domain.term.entity.TermType;
import io.github.soundbar91.aipipeline.domain.term.entity.UserTermAgreement;
import io.github.soundbar91.aipipeline.domain.term.repository.TermRepository;
import io.github.soundbar91.aipipeline.domain.term.repository.UserTermAgreementRepository;
import io.github.soundbar91.aipipeline.domain.user.entity.SocialProvider;
import io.github.soundbar91.aipipeline.domain.user.entity.User;
import io.github.soundbar91.aipipeline.domain.user.entity.UserStatus;
import io.github.soundbar91.aipipeline.domain.user.repository.UserRepository;
import io.github.soundbar91.aipipeline.global.exception.BusinessException;
import io.github.soundbar91.aipipeline.global.exception.ErrorCode;
import io.github.soundbar91.aipipeline.global.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService {

    private final Map<SocialProvider, SocialAuthService> socialAuthServices;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TermRepository termRepository;
    private final UserTermAgreementRepository userTermAgreementRepository;
    private final SchoolRepository schoolRepository;
    private final JwtProvider jwtProvider;
    private final long refreshTokenExpiration;

    public AuthService(
            List<SocialAuthService> socialAuthServices,
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            TermRepository termRepository,
            UserTermAgreementRepository userTermAgreementRepository,
            SchoolRepository schoolRepository,
            JwtProvider jwtProvider,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.socialAuthServices = socialAuthServices.stream()
                .collect(Collectors.toMap(SocialAuthService::getProvider, Function.identity()));
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.termRepository = termRepository;
        this.userTermAgreementRepository = userTermAgreementRepository;
        this.schoolRepository = schoolRepository;
        this.jwtProvider = jwtProvider;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public SocialLoginResponse socialLogin(SocialLoginRequest request) {
        SocialProvider provider = parseProvider(request.provider());
        SocialAuthService socialAuthService = socialAuthServices.get(provider);
        if (socialAuthService == null) {
            throw new BusinessException(ErrorCode.INVALID_SOCIAL_PROVIDER);
        }

        SocialUserInfo userInfo = socialAuthService.getUserInfo(request.accessToken());
        Optional<User> existingUser = userRepository.findBySocialIdAndProvider(userInfo.socialId(), provider);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.getStatus() == UserStatus.ACTIVE) {
                return buildLoginResponse(user);
            }
            // PENDING 유저: 회원가입 재시도
            String signupToken = jwtProvider.createSignupToken(userInfo.socialId(), provider.name());
            List<Term> terms = termRepository.findByActiveTrue();
            return SocialLoginResponse.ofNewUser(signupToken, terms);
        }

        // 신규 유저 생성
        User newUser = User.builder()
                .socialId(userInfo.socialId())
                .provider(provider)
                .email(userInfo.email())
                .status(UserStatus.PENDING)
                .build();
        userRepository.save(newUser);

        String signupToken = jwtProvider.createSignupToken(userInfo.socialId(), provider.name());
        List<Term> terms = termRepository.findByActiveTrue();
        return SocialLoginResponse.ofNewUser(signupToken, terms);
    }

    public TermAgreementResponse agreeTerms(String signupToken, TermAgreementRequest request) {
        String subject = jwtProvider.getSignupSubject(signupToken);
        String[] parts = subject.split(":");
        String socialId = parts[0];
        SocialProvider provider = SocialProvider.valueOf(parts[1]);

        User user = userRepository.findBySocialIdAndProvider(socialId, provider)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Map<Long, Boolean> agreementMap = request.agreements().stream()
                .collect(Collectors.toMap(
                        TermAgreementRequest.TermAgreementItem::termId,
                        TermAgreementRequest.TermAgreementItem::agreed
                ));

        List<Term> activeTerms = termRepository.findByActiveTrue();

        // 필수 약관 전체 동의 확인
        boolean allRequiredAgreed = activeTerms.stream()
                .filter(t -> t.getType() == TermType.REQUIRED)
                .allMatch(t -> Boolean.TRUE.equals(agreementMap.get(t.getId())));

        if (!allRequiredAgreed) {
            throw new BusinessException(ErrorCode.REQUIRED_TERM_NOT_AGREED);
        }

        // 기존 동의 삭제 후 새로 저장 (재시도 케이스 대비)
        List<UserTermAgreement> existingAgreements = userTermAgreementRepository.findByUser(user);
        userTermAgreementRepository.deleteAll(existingAgreements);

        Set<Long> requestedTermIds = agreementMap.keySet();
        List<Term> termsToAgree = activeTerms.stream()
                .filter(t -> requestedTermIds.contains(t.getId()))
                .toList();

        List<UserTermAgreement> agreements = termsToAgree.stream()
                .map(term -> UserTermAgreement.builder()
                        .user(user)
                        .term(term)
                        .agreed(Boolean.TRUE.equals(agreementMap.get(term.getId())))
                        .build())
                .toList();

        userTermAgreementRepository.saveAll(agreements);

        // 새 signupToken 발급 (유효 시간 갱신)
        String newSignupToken = jwtProvider.createSignupToken(socialId, provider.name());
        return TermAgreementResponse.of(newSignupToken);
    }

    public SignupCompleteResponse completeSignup(String signupToken, SignupCompleteRequest request) {
        String subject = jwtProvider.getSignupSubject(signupToken);
        String[] parts = subject.split(":");
        String socialId = parts[0];
        SocialProvider provider = SocialProvider.valueOf(parts[1]);

        User user = userRepository.findBySocialIdAndProvider(socialId, provider)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        School school = schoolRepository.findById(request.schoolId())
                .orElseThrow(() -> new BusinessException(ErrorCode.SCHOOL_NOT_FOUND));

        user.completeSignup(request.name(), request.studentId(), school);

        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = saveOrUpdateRefreshToken(user);

        return SignupCompleteResponse.of(accessToken, refreshToken, user);
    }

    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        RefreshToken savedToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (savedToken.isExpired()) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = savedToken.getUser();
        String newAccessToken = jwtProvider.createAccessToken(user.getId());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getId());
        savedToken.updateToken(newRefreshToken, LocalDateTime.now().plusSeconds(refreshTokenExpiration));

        return new TokenRefreshResponse(newAccessToken, newRefreshToken);
    }

    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        refreshTokenRepository.deleteByUser(user);
    }

    private SocialLoginResponse buildLoginResponse(User user) {
        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = saveOrUpdateRefreshToken(user);
        return SocialLoginResponse.ofExistingUser(accessToken, refreshToken);
    }

    private String saveOrUpdateRefreshToken(User user) {
        String tokenValue = jwtProvider.createRefreshToken(user.getId());
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(refreshTokenExpiration);

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElse(null);

        if (refreshToken != null) {
            refreshToken.updateToken(tokenValue, expiresAt);
        } else {
            refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(tokenValue)
                    .expiresAt(expiresAt)
                    .build();
            refreshTokenRepository.save(refreshToken);
        }
        return tokenValue;
    }

    private SocialProvider parseProvider(String provider) {
        try {
            return SocialProvider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_SOCIAL_PROVIDER);
        }
    }
}
