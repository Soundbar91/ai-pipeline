package io.github.soundbar91.aipipeline.domain.club.repository;

import io.github.soundbar91.aipipeline.domain.club.entity.Club;
import io.github.soundbar91.aipipeline.domain.school.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {

    Optional<Club> findByIdAndSchool(Long clubId, School school);
}
