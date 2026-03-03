package io.github.soundbar91.aipipeline.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.soundbar91.aipipeline.domain.term.entity.Term;
import io.github.soundbar91.aipipeline.domain.term.entity.TermType;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SocialLoginResponse(
        boolean isNewUser,
        // 기존 유저
        String accessToken,
        String refreshToken,
        // 신규 유저
        String signupToken,
        List<TermInfo> requiredTerms
) {
    public static SocialLoginResponse ofExistingUser(String accessToken, String refreshToken) {
        return new SocialLoginResponse(false, accessToken, refreshToken, null, null);
    }

    public static SocialLoginResponse ofNewUser(String signupToken, List<Term> terms) {
        List<TermInfo> termInfos = terms.stream()
                .map(t -> new TermInfo(t.getId(), t.getTitle(), t.getType()))
                .toList();
        return new SocialLoginResponse(true, null, null, signupToken, termInfos);
    }

    public record TermInfo(Long id, String title, TermType type) {
    }
}
