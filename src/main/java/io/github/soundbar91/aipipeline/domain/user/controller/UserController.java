package io.github.soundbar91.aipipeline.domain.user.controller;

import io.github.soundbar91.aipipeline.domain.user.dto.response.UserProfileResponse;
import io.github.soundbar91.aipipeline.domain.user.service.UserService;
import io.github.soundbar91.aipipeline.global.response.ApiResponse;
import io.github.soundbar91.aipipeline.global.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserProfileResponse response = userService.getMyProfile(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
