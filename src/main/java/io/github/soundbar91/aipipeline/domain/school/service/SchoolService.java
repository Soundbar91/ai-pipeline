package io.github.soundbar91.aipipeline.domain.school.service;

import io.github.soundbar91.aipipeline.domain.school.dto.response.SchoolSearchResponse;
import io.github.soundbar91.aipipeline.domain.school.repository.SchoolRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    public SchoolSearchResponse search(String query, int page, int size) {
        String searchQuery = query != null ? query : "";
        Page<SchoolSearchResponse.SchoolInfo> result = schoolRepository
                .findByNameContaining(searchQuery, PageRequest.of(page, size))
                .map(SchoolSearchResponse.SchoolInfo::from);

        return new SchoolSearchResponse(
                result.getContent(),
                result.getTotalElements(),
                result.hasNext()
        );
    }
}
