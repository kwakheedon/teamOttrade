package com.ottrade.ottrade.domain.log.dto;

import lombok.Getter;

@Getter
public class TopSearchKeywordDTO {
    private final String keyword;
    private final long searchCount;

    public TopSearchKeywordDTO(String keyword, long searchCount) {
        this.keyword = keyword;
        this.searchCount = searchCount;
    }
}