package com.ottrade.ottrade.domain.hssearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class YearlyTradeDataDTO {
    private final String year;
    private final long totalExpDlr;
    private final long totalExpWgt;
    private final long totalImpDlr;
    private final long totalImpWgt;
}