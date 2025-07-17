package com.ottrade.ottrade.domain.log.dto;

import com.ottrade.ottrade.domain.log.entity.SearchLog;
import lombok.Getter;
import java.sql.Timestamp;

@Getter
public class SearchLogResponseDTO {
    private final Long id;
    private final String keyword;
    private final String korePrnm;
    private final Timestamp searchedAt;
    private final String gptSummary;

    public SearchLogResponseDTO(SearchLog log) {
        this.id = log.getId();
        this.keyword = log.getKeyword();
        this.korePrnm = log.getKorePrnm();
        this.searchedAt = log.getSearchedAt();
        this.gptSummary = log.getGptSummary();
    }
}