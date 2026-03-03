package io.github.soundbar91.aipipeline.domain.term.dto.response;

import io.github.soundbar91.aipipeline.domain.term.entity.Term;
import io.github.soundbar91.aipipeline.domain.term.entity.TermType;

public record TermResponse(
        Long id,
        String title,
        String content,
        TermType type
) {
    public static TermResponse from(Term term) {
        return new TermResponse(term.getId(), term.getTitle(), term.getContent(), term.getType());
    }
}
