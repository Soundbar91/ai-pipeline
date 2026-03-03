package io.github.soundbar91.aipipeline.domain.auth.service.social;

import io.github.soundbar91.aipipeline.domain.user.entity.SocialProvider;
import io.github.soundbar91.aipipeline.global.exception.BusinessException;
import io.github.soundbar91.aipipeline.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
public class NaverAuthService implements SocialAuthService {

    private final WebClient webClient;
    private final String userInfoUri;

    public NaverAuthService(
            WebClient webClient,
            @Value("${social.naver.user-info-uri}") String userInfoUri
    ) {
        this.webClient = webClient;
        this.userInfoUri = userInfoUri;
    }

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.NAVER;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SocialUserInfo getUserInfo(String accessToken) {
        try {
            Map<String, Object> response = webClient.get()
                    .uri(userInfoUri)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) {
                throw new BusinessException(ErrorCode.SOCIAL_AUTH_FAILED);
            }

            Map<String, Object> naverResponse = (Map<String, Object>) response.get("response");
            if (naverResponse == null) {
                throw new BusinessException(ErrorCode.SOCIAL_AUTH_FAILED);
            }

            String socialId = (String) naverResponse.get("id");
            String email = (String) naverResponse.get("email");
            return new SocialUserInfo(socialId, email);
        } catch (WebClientResponseException e) {
            throw new BusinessException(ErrorCode.SOCIAL_AUTH_FAILED);
        }
    }
}
