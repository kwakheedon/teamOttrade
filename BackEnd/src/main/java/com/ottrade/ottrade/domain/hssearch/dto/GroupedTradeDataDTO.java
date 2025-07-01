package com.ottrade.ottrade.domain.hssearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GroupedTradeDataDTO {
    // --- 국가 기본 정보 ---
    private String cntyCd;          // 국가 코드
    private String statKor;         // 국가 한글명
    private String hsCd;            // HS 코드

    // --- 전년도 총계 정보 ---
    private final long lastYearExpDlr;   // 전년도 총 수출 금액
    private final long lastYearExpWgt;   // 전년도 총 수출 중량
    private final long lastYearImpDlr;   // 전년도 총 수입 금액
    private final long lastYearImpWgt;   // 전년도 총 수입 중량

    // --- 6년간 통계 추이 ---
    private List<YearlyTradeDataDTO> items; // 연도별 데이터 리스트
}