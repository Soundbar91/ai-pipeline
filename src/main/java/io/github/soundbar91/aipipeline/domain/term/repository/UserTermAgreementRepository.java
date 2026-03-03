package io.github.soundbar91.aipipeline.domain.term.repository;

import io.github.soundbar91.aipipeline.domain.term.entity.UserTermAgreement;
import io.github.soundbar91.aipipeline.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTermAgreementRepository extends JpaRepository<UserTermAgreement, Long> {
    List<UserTermAgreement> findByUser(User user);
}
