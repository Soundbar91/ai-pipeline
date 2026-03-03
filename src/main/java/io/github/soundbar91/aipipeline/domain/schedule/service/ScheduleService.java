package io.github.soundbar91.aipipeline.domain.schedule.service;

import io.github.soundbar91.aipipeline.domain.schedule.dto.response.MonthlyScheduleResponse;
import io.github.soundbar91.aipipeline.domain.schedule.dto.response.ScheduleDetailResponse;
import io.github.soundbar91.aipipeline.domain.schedule.dto.response.ScheduleSummaryResponse;
import io.github.soundbar91.aipipeline.domain.schedule.entity.Schedule;
import io.github.soundbar91.aipipeline.domain.schedule.entity.ScheduleCategory;
import io.github.soundbar91.aipipeline.domain.schedule.repository.ScheduleRepository;
import io.github.soundbar91.aipipeline.global.exception.BusinessException;
import io.github.soundbar91.aipipeline.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public MonthlyScheduleResponse getMonthlySchedules(int year, int month, List<ScheduleCategory> categories) {
        validateYearMonth(year, month);

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        List<Schedule> schedules = scheduleRepository.findByMonthAndCategories(
                firstDay, lastDay, categories, categories.isEmpty());

        Map<LocalDate, List<ScheduleSummaryResponse>> result = new LinkedHashMap<>();
        for (LocalDate d = firstDay; !d.isAfter(lastDay); d = d.plusDays(1)) {
            result.put(d, new ArrayList<>());
        }

        for (Schedule s : schedules) {
            LocalDate from = s.getStartDate().isBefore(firstDay) ? firstDay : s.getStartDate();
            LocalDate to = s.getEndDate().isAfter(lastDay) ? lastDay : s.getEndDate();
            for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
                result.get(d).add(ScheduleSummaryResponse.from(s));
            }
        }

        return new MonthlyScheduleResponse(year, month, result);
    }

    public List<ScheduleDetailResponse> getDailySchedules(LocalDate date, List<ScheduleCategory> categories) {
        List<Schedule> schedules = scheduleRepository.findByDateAndCategories(
                date, categories, categories.isEmpty());
        return schedules.stream().map(ScheduleDetailResponse::from).toList();
    }

    public List<ScheduleDetailResponse> getMonthlyScheduleList(int year, int month, List<ScheduleCategory> categories) {
        validateYearMonth(year, month);

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        List<Schedule> schedules = scheduleRepository.findByMonthAndCategories(
                firstDay, lastDay, categories, categories.isEmpty());
        return schedules.stream().map(ScheduleDetailResponse::from).toList();
    }

    private void validateYearMonth(int year, int month) {
        if (year < 2000 || year > 2100) {
            throw new BusinessException(ErrorCode.INVALID_SCHEDULE_PARAMETER);
        }
        if (month < 1 || month > 12) {
            throw new BusinessException(ErrorCode.INVALID_SCHEDULE_PARAMETER);
        }
    }
}
