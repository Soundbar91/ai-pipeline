package io.github.soundbar91.aipipeline.domain.auth.dto.response;

public record TokenRefreshResponse(
        String accessToken,
        String refreshToken
) {
}
