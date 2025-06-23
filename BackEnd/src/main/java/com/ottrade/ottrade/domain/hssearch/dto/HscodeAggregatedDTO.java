package com.ottrade.ottrade.domain.hssearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HscodeAggregatedDTO {
    private String hsSgn;
    private String korePrnm;
    private int avgTxrt;
    private int rate; // 백분율 (ex: 18.3)
}
