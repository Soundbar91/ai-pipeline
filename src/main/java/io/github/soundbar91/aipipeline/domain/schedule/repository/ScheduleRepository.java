package io.github.soundbar91.aipipeline.domain.schedule.repository;

import io.github.soundbar91.aipipeline.domain.schedule.entity.Schedule;
import io.github.soundbar91.aipipeline.domain.schedule.entity.ScheduleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("""
            SELECT s FROM Schedule s
            WHERE s.startDate <= :lastDay AND s.endDate >= :firstDay
              AND (:categoriesEmpty = true OR s.category IN :categories)
            ORDER BY s.startDate ASC, s.allDay DESC, s.startTime ASC NULLS LAST
            """)
    List<Schedule> findByMonthAndCategories(
            @Param("firstDay") LocalDate firstDay,
            @Param("lastDay") LocalDate lastDay,
            @Param("categories") List<ScheduleCategory> categories,
            @Param("categoriesEmpty") boolean categoriesEmpty
    );

    @Query("""
            SELECT s FROM Schedule s
            WHERE s.startDate <= :date AND s.endDate >= :date
              AND (:categoriesEmpty = true OR s.category IN :categories)
            ORDER BY s.allDay DESC, s.startTime ASC NULLS LAST
            """)
    List<Schedule> findByDateAndCategories(
            @Param("date") LocalDate date,
            @Param("categories") List<ScheduleCategory> categories,
            @Param("categoriesEmpty") boolean categoriesEmpty
    );
}
