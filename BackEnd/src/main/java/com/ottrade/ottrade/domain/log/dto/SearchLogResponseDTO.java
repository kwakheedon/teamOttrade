package com.ottrade.ottrade.domain.log.dto;

import com.ottrade.ottrade.domain.log.entity.SearchLog;
import lombok.Getter;
import java.sql.Timestamp;

@Getter
public class SearchLogResponseDTO {
    private final Long id;
    private final String keyword;       // HS Code
    private final String korePrnm;      // HS 품목 해설 (이 필드를 추가합니다!)
    private final Timestamp searchedAt;
    private final String gptSummary;

    public SearchLogResponseDTO(SearchLog log) {
        this.id = log.getId();
        this.keyword = log.getKeyword();
        this.korePrnm = log.getKorePrnm(); // 엔티티에서 korePrnm 값을 가져와 설정합니다.
        this.searchedAt = log.getSearchedAt();
        this.gptSummary = log.getGptSummary();
    }
}