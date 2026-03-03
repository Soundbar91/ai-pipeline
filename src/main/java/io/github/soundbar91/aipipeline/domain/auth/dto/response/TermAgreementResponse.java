package io.github.soundbar91.aipipeline.domain.auth.dto.response;

public record TermAgreementResponse(
        String signupToken,
        String nextStep
) {
    public static TermAgreementResponse of(String signupToken) {
        return new TermAgreementResponse(signupToken, "SCHOOL_SELECTION");
    }
}
