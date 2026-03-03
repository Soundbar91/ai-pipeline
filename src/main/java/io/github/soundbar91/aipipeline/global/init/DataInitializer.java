package io.github.soundbar91.aipipeline.global.init;

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

@Component
public class DataInitializer implements ApplicationRunner {

    private final SchoolRepository schoolRepository;
    private final TermRepository termRepository;

    public DataInitializer(SchoolRepository schoolRepository, TermRepository termRepository) {
        this.schoolRepository = schoolRepository;
        this.termRepository = termRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        initTerms();
        initSchools();
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
}
