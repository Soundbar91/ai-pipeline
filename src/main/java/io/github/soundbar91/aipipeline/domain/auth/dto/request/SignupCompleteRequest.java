package io.github.soundbar91.aipipeline.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignupCompleteRequest(
        @NotNull(message = "schoolId는 필수입니다.")
        Long schoolId,

        @NotBlank(message = "studentId는 필수입니다.")
        String studentId,

        @NotBlank(message = "name은 필수입니다.")
        String name
) {
}
