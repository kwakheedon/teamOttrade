package com.ottrade.ottrade.domain.hssearch.controller;

import com.ottrade.ottrade.domain.hssearch.dto.HscodeAggregatedDTO;
import com.ottrade.ottrade.domain.hssearch.dto.TradeTop3ResultDTO;
import com.ottrade.ottrade.domain.hssearch.dto.YearlyTradeDataDTO;
import com.ottrade.ottrade.domain.hssearch.service.HsSearchService;
import com.ottrade.ottrade.domain.hssearch.service.TradeApiService;
import com.ottrade.ottrade.domain.log.service.LogService; // LogService 임포트
import com.ottrade.ottrade.global.util.ApiResponse;
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
    private final LogService logService; // LogService 의존성 주입

    @GetMapping(value = "/search-summary/{prnm}", produces = "application/json")
    public ResponseEntity<ApiResponse<List<HscodeAggregatedDTO>>> getSummary(@PathVariable String prnm) {

        // 검색 기록 저장 로직 호출
        logService.savePnmLog(prnm);

        try {
            List<HscodeAggregatedDTO> result = hsSearchService.getAggregatedHsCodeInfo(prnm);
            // ApiResponse의 success static 메소드는 2개의 파라미터를 받지 않으므로, 아래와 같이 수정합니다.
            return ResponseEntity.ok(ApiResponse.success("데이터 처리 성공", result));
        } catch (Exception e) {
            // ApiResponse의 fail static 메소드는 2개의 파라미터를 받지 않으므로, 아래와 같이 수정합니다.
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("데이터 처리 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
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
        // TODO: 사용자가 로그인한 상태라면 여기에 HS코드 검색 기록 저장 로직 추가
        // logService.saveHsCodeSearchLog(hsSgn, userId);

        TradeTop3ResultDTO result = tradeApiService.fetchTop3TradeStats(hsSgn);
        return ResponseEntity.ok(result);
    }

}