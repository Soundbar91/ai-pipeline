package io.github.soundbar91.aipipeline.domain.schedule.dto.response;

import io.github.soundbar91.aipipeline.domain.schedule.entity.Schedule;

public record ScheduleSummaryResponse(
        Long id,
        String title,
        String category,
        String color
) {
    public static ScheduleSummaryResponse from(Schedule schedule) {
        return new ScheduleSummaryResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getCategory().name(),
                schedule.getColor()
        );
    }
}
