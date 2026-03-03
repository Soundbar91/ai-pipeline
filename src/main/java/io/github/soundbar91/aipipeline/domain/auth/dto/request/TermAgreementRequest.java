package io.github.soundbar91.aipipeline.domain.auth.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record TermAgreementRequest(
        @NotEmpty(message = "약관 동의 목록은 필수입니다.")
        List<TermAgreementItem> agreements
) {
    public record TermAgreementItem(
            Long termId,
            boolean agreed
    ) {
    }
}
