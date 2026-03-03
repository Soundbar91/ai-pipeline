package io.github.soundbar91.aipipeline.domain.user.service;

import io.github.soundbar91.aipipeline.domain.user.dto.response.UserProfileResponse;
import io.github.soundbar91.aipipeline.domain.user.entity.User;
import io.github.soundbar91.aipipeline.domain.user.repository.UserRepository;
import io.github.soundbar91.aipipeline.global.exception.BusinessException;
import io.github.soundbar91.aipipeline.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponse getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return UserProfileResponse.from(user);
    }
}
