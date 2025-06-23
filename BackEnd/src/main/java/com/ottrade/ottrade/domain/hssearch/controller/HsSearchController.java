package com.ottrade.ottrade.domain.hssearch.controller;

import com.ottrade.ottrade.domain.hssearch.dto.HscodeAggregatedDTO;
import com.ottrade.ottrade.domain.hssearch.dto.HscodeInfoDTO;
import com.ottrade.ottrade.domain.hssearch.dto.TradeTop3ResultDTO;
import com.ottrade.ottrade.domain.hssearch.dto.YearlyTradeDataDTO;
import com.ottrade.ottrade.domain.hssearch.service.HsSearchService;
import com.ottrade.ottrade.domain.hssearch.service.TradeApiService;
import com.ottrade.ottrade.util.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HsSearchController {

    private final HsSearchService hsSearchService;
    private final TradeApiService tradeApiService;

    @GetMapping(value = "/search-summary/{prnm}", produces = "application/json")
    public ResponseEntity<ApiResponse<List<HscodeAggregatedDTO>>> getSummary(@PathVariable String prnm) {
        try {
            List<HscodeAggregatedDTO> result = hsSearchService.getAggregatedHsCodeInfo(prnm);
            return ResponseEntity.ok(ApiResponse.success(result, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail("데이터 처리 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // 기존: 연도별 grouped
    @GetMapping(value = "/grouped/{hsSgn}/{cntyCd}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<List<YearlyTradeDataDTO>> getGroupedData(
            @PathVariable String hsSgn,
            @PathVariable String cntyCd
    ) {
        List<YearlyTradeDataDTO> result = tradeApiService.fetchGroupedTradeList(hsSgn, cntyCd);
        return ResponseEntity.ok(result);
    }

    // 신규: FTA 국가 기준 Top3
    @GetMapping(value = "/top3/{hsSgn}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<TradeTop3ResultDTO> getTop3TradeStats(
            @PathVariable String hsSgn
    ) {
        TradeTop3ResultDTO result = tradeApiService.fetchTop3TradeStats(hsSgn);
        return ResponseEntity.ok(result);
    }

}