package io.github.soundbar91.aipipeline.domain.auth.dto.response;

import io.github.soundbar91.aipipeline.domain.user.entity.User;

public record SignupCompleteResponse(
        String accessToken,
        String refreshToken,
        UserInfo user
) {
    public static SignupCompleteResponse of(String accessToken, String refreshToken, User user) {
        return new SignupCompleteResponse(accessToken, refreshToken, UserInfo.from(user));
    }

    public record UserInfo(Long id, String name, String studentId, String email) {
        public static UserInfo from(User user) {
            return new UserInfo(user.getId(), user.getName(), user.getStudentId(), user.getEmail());
        }
    }
}
