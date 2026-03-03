package io.github.soundbar91.aipipeline.domain.schedule.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record MonthlyScheduleResponse(
        int year,
        int month,
        Map<LocalDate, List<ScheduleSummaryResponse>> schedules
) {
}
