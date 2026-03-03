package io.github.soundbar91.aipipeline.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SocialLoginRequest(
        @NotNull(message = "provider는 필수입니다.")
        String provider,

        @NotBlank(message = "accessToken은 필수입니다.")
        String accessToken
) {
}
