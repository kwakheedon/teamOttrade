package com.ottrade.ottrade.domain.hssearch.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import java.util.List;

@Getter
@AllArgsConstructor
public class TradeTop3ResultDTO {
    private final List<TradeTopCountryDTO> topExpDlr;
    private final List<TradeTopCountryDTO> topExpWgt;
    private final List<TradeTopCountryDTO> topImpDlr;
    private final List<TradeTopCountryDTO> topImpWgt;
}