package io.github.soundbar91.aipipeline.domain.term.repository;

import io.github.soundbar91.aipipeline.domain.term.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermRepository extends JpaRepository<Term, Long> {
    List<Term> findByActiveTrue();
}
