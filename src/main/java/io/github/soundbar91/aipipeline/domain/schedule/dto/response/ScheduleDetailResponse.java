package io.github.soundbar91.aipipeline.domain.schedule.dto.response;

import io.github.soundbar91.aipipeline.domain.schedule.entity.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleDetailResponse(
        Long id,
        String title,
        String description,
        String category,
        String categoryDisplayName,
        String color,
        LocalDate startDate,
        LocalDate endDate,
        LocalTime startTime,
        LocalTime endTime,
        String clubName,
        boolean allDay
) {
    public static ScheduleDetailResponse from(Schedule schedule) {
        return new ScheduleDetailResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getDescription(),
                schedule.getCategory().name(),
                schedule.getCategory().getDisplayName(),
                schedule.getColor(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getClubName(),
                schedule.isAllDay()
        );
    }
}
