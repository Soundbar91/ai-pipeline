package io.github.soundbar91.aipipeline.domain.study.service;

import io.github.soundbar91.aipipeline.domain.study.dto.RankingRow;
import io.github.soundbar91.aipipeline.domain.study.dto.response.MyStudyStatsResponse;
import io.github.soundbar91.aipipeline.domain.study.dto.response.StudyRankingResponse;
import io.github.soundbar91.aipipeline.domain.study.dto.response.StudySessionStartResponse;
import io.github.soundbar91.aipipeline.domain.study.dto.response.StudySessionStopResponse;
import io.github.soundbar91.aipipeline.domain.study.entity.RankingType;
import io.github.soundbar91.aipipeline.domain.study.entity.StudySession;
import io.github.soundbar91.aipipeline.domain.study.repository.StudySessionRepository;
import io.github.soundbar91.aipipeline.domain.user.entity.User;
import io.github.soundbar91.aipipeline.domain.user.repository.UserRepository;
import io.github.soundbar91.aipipeline.global.exception.BusinessException;
import io.github.soundbar91.aipipeline.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudyService {

    private final StudySessionRepository studySessionRepository;
    private final UserRepository userRepository;

    public StudyService(StudySessionRepository studySessionRepository, UserRepository userRepository) {
        this.studySessionRepository = studySessionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public StudySessionStartResponse startSession(Long userId) {
        User user = findUser(userId);
        studySessionRepository.findByUserAndEndTimeIsNull(user).ifPresent(s -> {
            throw new BusinessException(ErrorCode.STUDY_SESSION_ALREADY_RUNNING);
        });

        LocalDateTime now = LocalDateTime.now();
        StudySession session = StudySession.builder()
                .user(user)
                .startTime(now)
                .date(now.toLocalDate())
                .build();
        studySessionRepository.save(session);

        Long accumulated = studySessionRepository.sumTotalByUser(user);
        Long today = studySessionRepository.sumTodayByUser(user, now.toLocalDate());

        return new StudySessionStartResponse(accumulated, today, true, now);
    }

    @Transactional
    public StudySessionStopResponse stopSession(Long userId) {
        User user = findUser(userId);
        StudySession session = studySessionRepository.findByUserAndEndTimeIsNull(user)
                .orElseThrow(() -> new BusinessException(ErrorCode.STUDY_SESSION_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        session.stop(now);

        Long accumulated = studySessionRepository.sumTotalByUser(user);
        Long today = studySessionRepository.sumTodayByUser(user, now.toLocalDate());

        return new StudySessionStopResponse(session.getDuration(), accumulated, today);
    }

    @Transactional(readOnly = true)
    public MyStudyStatsResponse getMyStats(Long userId) {
        User user = findUser(userId);
        LocalDate today = LocalDate.now();

        Long accumulated = studySessionRepository.sumTotalByUser(user);
        Long todaySeconds = studySessionRepository.sumTodayByUser(user, today);

        Optional<StudySession> runningSession = studySessionRepository.findByUserAndEndTimeIsNull(user);
        if (runningSession.isPresent()) {
            long elapsed = java.time.Duration.between(runningSession.get().getStartTime(), LocalDateTime.now()).toSeconds();
            accumulated += elapsed;
            if (runningSession.get().getDate().equals(today)) {
                todaySeconds += elapsed;
            }
            return new MyStudyStatsResponse(accumulated, todaySeconds, true, runningSession.get().getStartTime());
        }

        return new MyStudyStatsResponse(accumulated, todaySeconds, false, null);
    }

    @Transactional(readOnly = true)
    public StudyRankingResponse getRankings(Long userId, RankingType type) {
        User user = findUser(userId);
        Long schoolId = user.getSchool().getId();
        LocalDate today = LocalDate.now();

        List<RankingRow> rows = switch (type) {
            case INDIVIDUAL -> studySessionRepository.findIndividualRanking(schoolId, today);
            case CLUB -> studySessionRepository.findClubRanking(schoolId, today);
            case STUDENT_YEAR -> studySessionRepository.findStudentYearRanking(schoolId, today);
        };

        List<StudyRankingResponse.RankingEntry> entries = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            RankingRow row = rows.get(i);
            entries.add(new StudyRankingResponse.RankingEntry(i + 1, row.name(), row.totalSeconds(), row.todaySeconds()));
        }

        return new StudyRankingResponse(type, entries);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
