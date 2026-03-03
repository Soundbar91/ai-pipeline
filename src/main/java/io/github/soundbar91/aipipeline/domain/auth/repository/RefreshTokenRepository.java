package io.github.soundbar91.aipipeline.domain.auth.repository;

import io.github.soundbar91.aipipeline.domain.auth.entity.RefreshToken;
import io.github.soundbar91.aipipeline.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
