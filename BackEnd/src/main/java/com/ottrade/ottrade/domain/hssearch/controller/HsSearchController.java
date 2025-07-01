package com.ottrade.ottrade.domain.hssearch.controller;

import com.ottrade.ottrade.domain.hssearch.dto.HscodeAggregatedDTO;
import com.ottrade.ottrade.domain.hssearch.dto.TradeTop3ResultDTO;
import com.ottrade.ottrade.domain.hssearch.dto.YearlyTradeDataDTO;
import com.ottrade.ottrade.domain.hssearch.service.HsSearchService;
import com.ottrade.ottrade.domain.hssearch.service.TradeApiService;
import com.ottrade.ottrade.domain.log.service.LogService;
import com.ottrade.ottrade.global.util.ApiResponse;
import com.ottrade.ottrade.security.user.CustomUserDetails; // CustomUserDetails 임포트
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // AuthenticationPrincipal 임포트
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HsSearchController {

    private final HsSearchService hsSearchService;
    private final TradeApiService tradeApiService;
    private final LogService logService;

    @GetMapping(value = "/search-summary/{prnm}", produces = "application/json")
    public ResponseEntity<ApiResponse<List<HscodeAggregatedDTO>>> getSummary(@PathVariable String prnm) {
        logService.savePnmLog(prnm);
        try {
            List<HscodeAggregatedDTO> result = hsSearchService.getAggregatedHsCodeInfo(prnm);
            return ResponseEntity.ok(ApiResponse.success("데이터 처리 성공", result));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("데이터 처리 실패: " + e.getMessage()));
        }
    }

    @GetMapping(value = "/grouped/{hsSgn}/{cntyCd}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<List<YearlyTradeDataDTO>> getGroupedData(
            @PathVariable String hsSgn,
            @PathVariable String cntyCd
    ) {
        List<YearlyTradeDataDTO> result = tradeApiService.fetchGroupedTradeList(hsSgn, cntyCd);
        return ResponseEntity.ok(result);
    }

    // 신규: FTA 국가 기준 Top3 (로그 저장 기능 추가)
    @GetMapping(value = "/top3/{hsSgn}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<TradeTop3ResultDTO> getTop3TradeStats(
            @PathVariable String hsSgn,
            @AuthenticationPrincipal CustomUserDetails userDetails // 1. 로그인 사용자 정보 받아오기
    ) {
        // 2. 로그인한 사용자인 경우, 로그 저장 메소드 호출
        if (userDetails != null) {
            logService.saveHsCodeSearchLog(hsSgn, userDetails.getUser().getId());
        }

        TradeTop3ResultDTO result = tradeApiService.fetchTop3TradeStats(hsSgn);
        return ResponseEntity.ok(result);
    }
}