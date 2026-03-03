package io.github.soundbar91.aipipeline.domain.school.repository;

import io.github.soundbar91.aipipeline.domain.school.entity.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
    Page<School> findByNameContaining(String name, Pageable pageable);
}
