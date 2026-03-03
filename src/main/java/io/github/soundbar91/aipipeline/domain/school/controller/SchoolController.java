package io.github.soundbar91.aipipeline.domain.school.controller;

import io.github.soundbar91.aipipeline.domain.school.dto.response.SchoolSearchResponse;
import io.github.soundbar91.aipipeline.domain.school.service.SchoolService;
import io.github.soundbar91.aipipeline.global.exception.BusinessException;
import io.github.soundbar91.aipipeline.global.exception.ErrorCode;
import io.github.soundbar91.aipipeline.global.response.ApiResponse;
import io.github.soundbar91.aipipeline.global.security.jwt.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {

    private final SchoolService schoolService;
    private final JwtProvider jwtProvider;

    public SchoolController(SchoolService schoolService, JwtProvider jwtProvider) {
        this.schoolService = schoolService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<SchoolSearchResponse>> search(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        String token = extractSignupToken(authHeader);
        // signupToken 유효성 검증 (getSignupSubject 내부에서 scope 검증)
        jwtProvider.getSignupSubject(token);

        SchoolSearchResponse response = schoolService.search(query, page, size);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    private String extractSignupToken(String authHeader) {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (!jwtProvider.isSignupToken(token)) {
                throw new BusinessException(ErrorCode.INSUFFICIENT_SCOPE);
            }
            return token;
        }
        throw new BusinessException(ErrorCode.INVALID_TOKEN);
    }
}
