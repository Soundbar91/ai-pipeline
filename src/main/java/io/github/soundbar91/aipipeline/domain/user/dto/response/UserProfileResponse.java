package io.github.soundbar91.aipipeline.domain.user.dto.response;

import io.github.soundbar91.aipipeline.domain.user.entity.SocialProvider;
import io.github.soundbar91.aipipeline.domain.user.entity.User;

import java.time.LocalDateTime;

public record UserProfileResponse(
        Long id,
        String name,
        String studentId,
        String email,
        SocialProvider provider,
        SchoolInfo school,
        LocalDateTime createdAt
) {
    public static UserProfileResponse from(User user) {
        SchoolInfo schoolInfo = user.getSchool() != null
                ? new SchoolInfo(user.getSchool().getId(), user.getSchool().getName())
                : null;
        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getStudentId(),
                user.getEmail(),
                user.getProvider(),
                schoolInfo,
                user.getCreatedAt()
        );
    }

    public record SchoolInfo(Long id, String name) {
    }
}
