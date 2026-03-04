package io.github.soundbar91.aipipeline.domain.study.dto.response;

import io.github.soundbar91.aipipeline.domain.study.entity.RankingType;

import java.util.List;

public record StudyRankingResponse(
        RankingType type,
        List<RankingEntry> rankings
) {
    public record RankingEntry(
            int rank,
            String name,
            Long accumulatedSeconds,
            Long todaySeconds
    ) {
    }
}
