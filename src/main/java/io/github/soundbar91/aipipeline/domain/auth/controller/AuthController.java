package io.github.soundbar91.aipipeline.domain.auth.controller;

import io.github.soundbar91.aipipeline.domain.auth.dto.request.SignupCompleteRequest;
import io.github.soundbar91.aipipeline.domain.auth.dto.request.SocialLoginRequest;
import io.github.soundbar91.aipipeline.domain.auth.dto.request.TermAgreementRequest;
import io.github.soundbar91.aipipeline.domain.auth.dto.request.TokenRefreshRequest;
import io.github.soundbar91.aipipeline.domain.auth.dto.response.SignupCompleteResponse;
import io.github.soundbar91.aipipeline.domain.auth.dto.response.SocialLoginResponse;
import io.github.soundbar91.aipipeline.domain.auth.dto.response.TermAgreementResponse;
import io.github.soundbar91.aipipeline.domain.auth.dto.response.TokenRefreshResponse;
import io.github.soundbar91.aipipeline.domain.auth.service.AuthService;
import io.github.soundbar91.aipipeline.global.exception.BusinessException;
import io.github.soundbar91.aipipeline.global.exception.ErrorCode;
import io.github.soundbar91.aipipeline.global.response.ApiResponse;
import io.github.soundbar91.aipipeline.global.security.CustomUserDetails;
import io.github.soundbar91.aipipeline.global.security.jwt.JwtProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    public AuthController(AuthService authService, JwtProvider jwtProvider) {
        this.authService = authService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/social-login")
    public ResponseEntity<ApiResponse<SocialLoginResponse>> socialLogin(
            @Valid @RequestBody SocialLoginRequest request
    ) {
        SocialLoginResponse response = authService.socialLogin(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/terms/agree")
    public ResponseEntity<ApiResponse<TermAgreementResponse>> agreeTerms(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody TermAgreementRequest request
    ) {
        String signupToken = extractSignupToken(authHeader);
        TermAgreementResponse response = authService.agreeTerms(signupToken, request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/signup/complete")
    public ResponseEntity<ApiResponse<SignupCompleteResponse>> completeSignup(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody SignupCompleteRequest request
    ) {
        String signupToken = extractSignupToken(authHeader);
        SignupCompleteResponse response = authService.completeSignup(signupToken, request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        TokenRefreshResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        authService.logout(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    private String extractSignupToken(String authHeader) {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (!jwtProvider.isSignupToken(token)) {
                throw new BusinessException(ErrorCode.INSUFFICIENT_SCOPE);
            }
            return token;
        }
        throw new BusinessException(ErrorCode.INVALID_TOKEN);
    }
}
