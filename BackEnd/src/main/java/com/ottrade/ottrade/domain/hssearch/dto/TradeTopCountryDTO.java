package com.ottrade.ottrade.domain.hssearch.dto;

import lombok.Getter;
import java.util.List;
import java.util.Objects;

@Getter
public class TradeTopCountryDTO {
    private final String cntyCd;
    private final int rank;
    private final long expDlr;
    private final long expWgt;
    private final long impDlr;
    private final long impWgt;
    private final String hsCd;
    private final String statCd;
    private final String statCdCntnKor1;
    private final String statKor;
    private final List<YearlyTradeDataDTO> items;

    public TradeTopCountryDTO(ItemDTO item, int rank, List<YearlyTradeDataDTO> items) {
        this.cntyCd = item.getCntyCd();
        this.rank = rank;
        this.expDlr = item.getExpDlr();
        this.expWgt = item.getExpWgt();
        this.impDlr = item.getImpDlr();
        this.impWgt = item.getImpWgt();
        this.hsCd = item.getHsCd();
        this.statCd = item.getStatCd();
        this.statCdCntnKor1 = item.getStatCdCntnKor1();
        this.statKor = item.getStatCdCntnKor1();
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeTopCountryDTO that = (TradeTopCountryDTO) o;
        return Objects.equals(getStatCd(), that.getStatCd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatCd());
    }
}