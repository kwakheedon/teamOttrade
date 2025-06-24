package com.ottrade.ottrade.domain.hssearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemDTO {
    private final String balPayments;
    private final long expDlr;
    private final long expWgt;
    private final String hsCd;
    private final long impDlr;
    private final long impWgt;
    private final String statCd;
    private final String statCdCntnKor1;
    private final String statKor;
    private final String year;
    private final String cntyCd;
}