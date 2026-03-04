package io.github.soundbar91.aipipeline.domain.study.controller;

import io.github.soundbar91.aipipeline.domain.study.dto.response.MyStudyStatsResponse;
import io.github.soundbar91.aipipeline.domain.study.dto.response.StudyRankingResponse;
import io.github.soundbar91.aipipeline.domain.study.dto.response.StudySessionStartResponse;
import io.github.soundbar91.aipipeline.domain.study.dto.response.StudySessionStopResponse;
import io.github.soundbar91.aipipeline.domain.study.entity.RankingType;
import io.github.soundbar91.aipipeline.domain.study.service.StudyService;
import io.github.soundbar91.aipipeline.global.response.ApiResponse;
import io.github.soundbar91.aipipeline.global.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/study")
public class StudyController {

    private final StudyService studyService;

    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    @PostMapping("/sessions/start")
    public ResponseEntity<ApiResponse<StudySessionStartResponse>> startSession(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        StudySessionStartResponse response = studyService.startSession(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/sessions/stop")
    public ResponseEntity<ApiResponse<StudySessionStopResponse>> stopSession(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        StudySessionStopResponse response = studyService.stopSession(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MyStudyStatsResponse>> getMyStats(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MyStudyStatsResponse response = studyService.getMyStats(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/rankings")
    public ResponseEntity<ApiResponse<StudyRankingResponse>> getRankings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam RankingType type
    ) {
        StudyRankingResponse response = studyService.getRankings(userDetails.getUserId(), type);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
