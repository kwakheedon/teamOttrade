package com.ottrade.ottrade.domain.hssearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class YearlyTradeDataDTO {
    private final String year;        // ex. "2023"
    private final long totalExpDlr;   // 연간 수출 금액 합계
    private final long totalExpWgt;   // 연간 수출 중량 합계
    private final long totalImpDlr;   // 연간 수입 금액 합계
    private final long totalImpWgt;   // 연간 수입 중량 합계
}