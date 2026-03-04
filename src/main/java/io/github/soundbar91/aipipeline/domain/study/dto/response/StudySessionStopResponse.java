package io.github.soundbar91.aipipeline.domain.study.dto.response;

public record StudySessionStopResponse(
        Long sessionDurationSeconds,
        Long accumulatedSeconds,
        Long todaySeconds
) {
}
