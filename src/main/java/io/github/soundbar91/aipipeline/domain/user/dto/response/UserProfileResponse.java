package io.github.soundbar91.aipipeline.domain.user.dto.response;

import io.github.soundbar91.aipipeline.domain.user.entity.User;

public record UserProfileResponse(
        String name,
        String studentId,
        String department,
        String schoolName,
        String phoneNumber
) {
    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(
                user.getName(),
                user.getStudentId(),
                user.getDepartment(),
                user.getSchool() != null ? user.getSchool().getName() : null,
                user.getPhoneNumber()
        );
    }
}
