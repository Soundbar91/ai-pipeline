package io.github.soundbar91.aipipeline.domain.term.service;

import io.github.soundbar91.aipipeline.domain.term.dto.response.TermResponse;
import io.github.soundbar91.aipipeline.domain.term.repository.TermRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TermService {

    private final TermRepository termRepository;

    public TermService(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    public List<TermResponse> getActiveTerms() {
        return termRepository.findByActiveTrue().stream()
                .map(TermResponse::from)
                .toList();
    }
}
