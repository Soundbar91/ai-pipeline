package io.github.soundbar91.aipipeline.domain.study.dto.response;

import java.time.LocalDateTime;

public record StudySessionStartResponse(
        Long accumulatedSeconds,
        Long todaySeconds,
        boolean isRunning,
        LocalDateTime sessionStartTime
) {
}
