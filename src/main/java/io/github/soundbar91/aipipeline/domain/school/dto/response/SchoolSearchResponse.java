package io.github.soundbar91.aipipeline.domain.school.dto.response;

import io.github.soundbar91.aipipeline.domain.school.entity.School;
import io.github.soundbar91.aipipeline.domain.school.entity.SchoolType;

import java.util.List;

public record SchoolSearchResponse(
        List<SchoolInfo> schools,
        long totalCount,
        boolean hasNext
) {
    public record SchoolInfo(Long id, String name, String location, SchoolType type) {
        public static SchoolInfo from(School school) {
            return new SchoolInfo(school.getId(), school.getName(), school.getLocation(), school.getType());
        }
    }
}
