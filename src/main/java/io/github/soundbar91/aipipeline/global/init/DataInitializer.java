package io.github.soundbar91.aipipeline.global.init;

import io.github.soundbar91.aipipeline.domain.schedule.entity.Schedule;
import io.github.soundbar91.aipipeline.domain.schedule.entity.ScheduleCategory;
import io.github.soundbar91.aipipeline.domain.schedule.repository.ScheduleRepository;
import io.github.soundbar91.aipipeline.domain.school.entity.School;
import io.github.soundbar91.aipipeline.domain.school.entity.SchoolType;
import io.github.soundbar91.aipipeline.domain.school.repository.SchoolRepository;
import io.github.soundbar91.aipipeline.domain.term.entity.Term;
import io.github.soundbar91.aipipeline.domain.term.entity.TermType;
import io.github.soundbar91.aipipeline.domain.term.repository.TermRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements ApplicationRunner {

    private final SchoolRepository schoolRepository;
    private final TermRepository termRepository;
    private final ScheduleRepository scheduleRepository;

    public DataInitializer(SchoolRepository schoolRepository, TermRepository termRepository,
                           ScheduleRepository scheduleRepository) {
        this.schoolRepository = schoolRepository;
        this.termRepository = termRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        initTerms();
        initSchools();
        initSchedules();
    }

    private void initTerms() {
        if (termRepository.count() > 0) return;

        termRepository.save(Term.builder()
                .title("서비스 이용약관 (필수)")
                .content("KONECT 서비스 이용약관 내용입니다. 본 약관은 서비스 이용에 관한 기본 사항을 규정합니다.")
                .type(TermType.REQUIRED)
                .active(true)
                .build());

        termRepository.save(Term.builder()
                .title("개인정보 처리방침 (필수)")
                .content("개인정보 처리방침 내용입니다. 수집하는 개인정보 항목 및 이용 목적을 안내합니다.")
                .type(TermType.REQUIRED)
                .active(true)
                .build());

        termRepository.save(Term.builder()
                .title("마케팅 수신 동의 (선택)")
                .content("마케팅 정보 수신 동의 내용입니다. 이벤트 및 혜택 정보를 제공합니다.")
                .type(TermType.OPTIONAL)
                .active(true)
                .build());
    }

    private void initSchools() {
        if (schoolRepository.count() > 0) return;

        schoolRepository.save(School.builder().name("서울대학교").location("서울특별시 관악구").type(SchoolType.UNIVERSITY).build());
        schoolRepository.save(School.builder().name("연세대학교").location("서울특별시 서대문구").type(SchoolType.UNIVERSITY).build());
        schoolRepository.save(School.builder().name("고려대학교").location("서울특별시 성북구").type(SchoolType.UNIVERSITY).build());
        schoolRepository.save(School.builder().name("성균관대학교").location("서울특별시 종로구").type(SchoolType.UNIVERSITY).build());
        schoolRepository.save(School.builder().name("한양대학교").location("서울특별시 성동구").type(SchoolType.UNIVERSITY).build());
        schoolRepository.save(School.builder().name("중앙대학교").location("서울특별시 동작구").type(SchoolType.UNIVERSITY).build());
        schoolRepository.save(School.builder().name("경희대학교").location("서울특별시 동대문구").type(SchoolType.UNIVERSITY).build());
        schoolRepository.save(School.builder().name("한국외국어대학교").location("서울특별시 동대문구").type(SchoolType.UNIVERSITY).build());
        schoolRepository.save(School.builder().name("서강대학교").location("서울특별시 마포구").type(SchoolType.UNIVERSITY).build());
        schoolRepository.save(School.builder().name("이화여자대학교").location("서울특별시 서대문구").type(SchoolType.UNIVERSITY).build());
        schoolRepository.save(School.builder().name("부산대학교").location("부산광역시 금정구").type(SchoolType.UNIVERSITY).build());
        schoolRepository.save(School.builder().name("경북대학교").location("대구광역시 북구").type(SchoolType.UNIVERSITY).build());
        schoolRepository.save(School.builder().name("전남대학교").location("광주광역시 북구").type(SchoolType.UNIVERSITY).build());
        schoolRepository.save(School.builder().name("충남대학교").location("대전광역시 유성구").type(SchoolType.UNIVERSITY).build());
        schoolRepository.save(School.builder().name("인하대학교").location("인천광역시 남구").type(SchoolType.UNIVERSITY).build());
    }

    private void initSchedules() {
        if (scheduleRepository.count() > 0) return;

        // HOLIDAY
        scheduleRepository.save(Schedule.builder()
                .title("신정")
                .category(ScheduleCategory.HOLIDAY)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 1, 1))
                .allDay(true)
                .build());

        scheduleRepository.save(Schedule.builder()
                .title("설날 연휴")
                .category(ScheduleCategory.HOLIDAY)
                .startDate(LocalDate.of(2026, 1, 28))
                .endDate(LocalDate.of(2026, 2, 1))
                .allDay(true)
                .build());

        // ACADEMIC
        scheduleRepository.save(Schedule.builder()
                .title("수강신청")
                .category(ScheduleCategory.ACADEMIC)
                .startDate(LocalDate.of(2026, 1, 4))
                .endDate(LocalDate.of(2026, 1, 5))
                .allDay(true)
                .build());

        scheduleRepository.save(Schedule.builder()
                .title("겨울방학")
                .category(ScheduleCategory.ACADEMIC)
                .startDate(LocalDate.of(2025, 12, 20))
                .endDate(LocalDate.of(2026, 1, 31))
                .allDay(true)
                .build());

        // STUDENT_UNION
        scheduleRepository.save(Schedule.builder()
                .title("정기 총동연 회의")
                .category(ScheduleCategory.STUDENT_UNION)
                .startDate(LocalDate.of(2026, 1, 8))
                .endDate(LocalDate.of(2026, 1, 8))
                .allDay(true)
                .build());

        scheduleRepository.save(Schedule.builder()
                .title("임원진 모집")
                .category(ScheduleCategory.STUDENT_UNION)
                .startDate(LocalDate.of(2026, 1, 15))
                .endDate(LocalDate.of(2026, 1, 20))
                .allDay(true)
                .build());

        // CLUB
        scheduleRepository.save(Schedule.builder()
                .title("BCSD 개총")
                .category(ScheduleCategory.CLUB)
                .clubName("BCSD")
                .startDate(LocalDate.of(2026, 1, 10))
                .endDate(LocalDate.of(2026, 1, 10))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(16, 0))
                .allDay(false)
                .build());

        scheduleRepository.save(Schedule.builder()
                .title("밴드 공연")
                .category(ScheduleCategory.CLUB)
                .clubName("밴드부")
                .startDate(LocalDate.of(2026, 1, 25))
                .endDate(LocalDate.of(2026, 1, 25))
                .allDay(true)
                .build());

        // DORMITORY
        scheduleRepository.save(Schedule.builder()
                .title("기숙사 입사 신청")
                .category(ScheduleCategory.DORMITORY)
                .startDate(LocalDate.of(2026, 1, 13))
                .endDate(LocalDate.of(2026, 1, 17))
                .allDay(true)
                .build());

        scheduleRepository.save(Schedule.builder()
                .title("기숙사 퇴사")
                .category(ScheduleCategory.DORMITORY)
                .startDate(LocalDate.of(2026, 1, 22))
                .endDate(LocalDate.of(2026, 1, 22))
                .allDay(true)
                .build());
    }
}
