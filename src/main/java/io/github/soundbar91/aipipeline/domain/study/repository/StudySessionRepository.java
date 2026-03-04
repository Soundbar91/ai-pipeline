package io.github.soundbar91.aipipeline.domain.study.repository;

import io.github.soundbar91.aipipeline.domain.study.dto.RankingRow;
import io.github.soundbar91.aipipeline.domain.study.entity.StudySession;
import io.github.soundbar91.aipipeline.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    Optional<StudySession> findByUserAndEndTimeIsNull(User user);

    @Query("SELECT COALESCE(SUM(s.duration), 0) FROM StudySession s WHERE s.user = :user AND s.endTime IS NOT NULL")
    Long sumTotalByUser(@Param("user") User user);

    @Query("SELECT COALESCE(SUM(s.duration), 0) FROM StudySession s WHERE s.user = :user AND s.date = :today AND s.endTime IS NOT NULL")
    Long sumTodayByUser(@Param("user") User user, @Param("today") LocalDate today);

    @Query("""
            SELECT new io.github.soundbar91.aipipeline.domain.study.dto.RankingRow(
                u.id, u.name,
                SUM(s.duration),
                SUM(CASE WHEN s.date = :today THEN s.duration ELSE 0L END))
            FROM StudySession s JOIN s.user u
            WHERE u.school.id = :schoolId AND s.endTime IS NOT NULL
            GROUP BY u.id, u.name
            ORDER BY SUM(s.duration) DESC
            """)
    List<RankingRow> findIndividualRanking(@Param("schoolId") Long schoolId, @Param("today") LocalDate today);

    @Query("""
            SELECT new io.github.soundbar91.aipipeline.domain.study.dto.RankingRow(
                c.id, c.name,
                SUM(s.duration),
                SUM(CASE WHEN s.date = :today THEN s.duration ELSE 0L END))
            FROM StudySession s JOIN s.user u JOIN u.club c
            WHERE u.school.id = :schoolId AND s.endTime IS NOT NULL
            GROUP BY c.id, c.name
            ORDER BY SUM(s.duration) DESC
            """)
    List<RankingRow> findClubRanking(@Param("schoolId") Long schoolId, @Param("today") LocalDate today);

    @Query("""
            SELECT new io.github.soundbar91.aipipeline.domain.study.dto.RankingRow(
                0L, SUBSTRING(u.studentId, 1, 4),
                SUM(s.duration),
                SUM(CASE WHEN s.date = :today THEN s.duration ELSE 0L END))
            FROM StudySession s JOIN s.user u
            WHERE u.school.id = :schoolId AND s.endTime IS NOT NULL
                  AND u.studentId IS NOT NULL
            GROUP BY SUBSTRING(u.studentId, 1, 4)
            ORDER BY SUM(s.duration) DESC
            """)
    List<RankingRow> findStudentYearRanking(@Param("schoolId") Long schoolId, @Param("today") LocalDate today);
}
