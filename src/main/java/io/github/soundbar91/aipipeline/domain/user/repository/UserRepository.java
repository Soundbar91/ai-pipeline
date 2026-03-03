package io.github.soundbar91.aipipeline.domain.user.repository;

import io.github.soundbar91.aipipeline.domain.user.entity.SocialProvider;
import io.github.soundbar91.aipipeline.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialIdAndProvider(String socialId, SocialProvider provider);
}
