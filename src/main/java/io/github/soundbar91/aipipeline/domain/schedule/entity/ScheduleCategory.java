package io.github.soundbar91.aipipeline.domain.schedule.entity;

import lombok.Getter;

@Getter
public enum ScheduleCategory {
    STUDENT_UNION("총동아리", "#7B61FF"),
    CLUB("동아리", "#FF6B35"),
    ACADEMIC("학사일정", "#00B0FF"),
    HOLIDAY("공휴일", "#F44336"),
    DORMITORY("기숙사", "#4CAF50");

    private final String displayName;
    private final String color;

    ScheduleCategory(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }
}
