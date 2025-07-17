package com.ottrade.ottrade.domain.hssearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GroupedTradeDataDTO {

    private String cntyCd;
    private String statKor;
    private String hsCd;

    private final long lastYearExpDlr;
    private final long lastYearExpWgt;
    private final long lastYearImpDlr;
    private final long lastYearImpWgt;

    private List<YearlyTradeDataDTO> items;
}