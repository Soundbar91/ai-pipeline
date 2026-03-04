package io.github.soundbar91.aipipeline.domain.study.entity;

import io.github.soundbar91.aipipeline.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_sessions", indexes = @Index(name = "idx_study_session_date", columnList = "date"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Builder
    public StudySession(User user, LocalDateTime startTime, LocalDate date) {
        this.user = user;
        this.startTime = startTime;
        this.date = date;
    }

    public void stop(LocalDateTime endTime) {
        this.endTime = endTime;
        this.duration = java.time.Duration.between(this.startTime, endTime).toSeconds();
    }
}
