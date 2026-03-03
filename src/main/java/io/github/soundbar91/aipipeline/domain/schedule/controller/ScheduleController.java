package io.github.soundbar91.aipipeline.domain.schedule.controller;

import io.github.soundbar91.aipipeline.domain.schedule.dto.response.MonthlyScheduleResponse;
import io.github.soundbar91.aipipeline.domain.schedule.dto.response.ScheduleDetailResponse;
import io.github.soundbar91.aipipeline.domain.schedule.entity.ScheduleCategory;
import io.github.soundbar91.aipipeline.domain.schedule.service.ScheduleService;
import io.github.soundbar91.aipipeline.global.exception.BusinessException;
import io.github.soundbar91.aipipeline.global.exception.ErrorCode;
import io.github.soundbar91.aipipeline.global.response.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<MonthlyScheduleResponse>> getMonthlyCalendar(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) List<String> categories
    ) {
        List<ScheduleCategory> categoryList = parseCategories(categories);
        MonthlyScheduleResponse response = scheduleService.getMonthlySchedules(year, month, categoryList);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<List<ScheduleDetailResponse>>> getDailySchedules(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) List<String> categories
    ) {
        List<ScheduleCategory> categoryList = parseCategories(categories);
        List<ScheduleDetailResponse> response = scheduleService.getDailySchedules(date, categoryList);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ScheduleDetailResponse>>> getMonthlyScheduleList(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) List<String> categories
    ) {
        List<ScheduleCategory> categoryList = parseCategories(categories);
        List<ScheduleDetailResponse> response = scheduleService.getMonthlyScheduleList(year, month, categoryList);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    private List<ScheduleCategory> parseCategories(List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return categories.stream()
                    .map(ScheduleCategory::valueOf)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_SCHEDULE_PARAMETER);
        }
    }
}
