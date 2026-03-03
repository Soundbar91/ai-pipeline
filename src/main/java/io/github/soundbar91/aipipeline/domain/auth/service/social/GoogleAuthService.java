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
public class GoogleAuthService implements SocialAuthService {

    private final WebClient webClient;
    private final String userInfoUri;

    public GoogleAuthService(
            WebClient webClient,
            @Value("${social.google.user-info-uri}") String userInfoUri
    ) {
        this.webClient = webClient;
        this.userInfoUri = userInfoUri;
    }

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.GOOGLE;
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

            String socialId = (String) response.get("sub");
            String email = (String) response.get("email");
            return new SocialUserInfo(socialId, email);
        } catch (WebClientResponseException e) {
            throw new BusinessException(ErrorCode.SOCIAL_AUTH_FAILED);
        }
    }
}
