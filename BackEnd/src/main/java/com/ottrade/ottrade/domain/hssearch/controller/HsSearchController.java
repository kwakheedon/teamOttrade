package com.ottrade.ottrade.domain.hssearch.controller;

import com.ottrade.ottrade.domain.hssearch.dto.GroupedTradeDataDTO;
import com.ottrade.ottrade.domain.hssearch.dto.HscodeAggregatedDTO;
import com.ottrade.ottrade.domain.hssearch.dto.TradeTop3ResultDTO;
import com.ottrade.ottrade.domain.hssearch.service.HsSearchService;
import com.ottrade.ottrade.domain.hssearch.service.TradeApiService;
import com.ottrade.ottrade.domain.log.service.LogService;
import com.ottrade.ottrade.global.util.ApiResponse;
import com.ottrade.ottrade.security.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Tag(name = "HS Code Search", description = "HS 코드 및 무역 통계 조회 API")
@RestController
public class HsSearchController {

    private final HsSearchService hsSearchService;
    private final TradeApiService tradeApiService;
    private final LogService logService;

    public HsSearchController(HsSearchService hsSearchService, TradeApiService tradeApiService, LogService logService) {
        this.hsSearchService = hsSearchService;
        this.tradeApiService = tradeApiService;
        this.logService = logService;
    }

    @Operation(summary = "품목명으로 HS코드 검색", description = "품목명을 입력하여 관련된 HS코드 목록을 조회합니다.")
    @GetMapping(value = "/search-summary/{prnm}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse<List<HscodeAggregatedDTO>>> getSummary(
            @Parameter(description = "검색할 품목명 (한글, 영어, 숫자, 공백만 허용)", required = true)
            @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s]{2,50}$", message = "검색어는 2~50자의 한글, 영어, 숫자, 공백만 사용할 수 있습니다.")
            @PathVariable String prnm) {

        try {
            List<HscodeAggregatedDTO> result = hsSearchService.getAggregatedHsCodeInfo(prnm);
            if (result != null && !result.isEmpty()) {
                logService.savePnmLog(prnm);
            }
            return ResponseEntity.ok(ApiResponse.success("데이터 처리 성공", result));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("데이터 처리 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "HS코드/국가별 상세 정보 조회", description = "특정 HS코드와 국가에 대한 전년도 총계 및 6년간의 연도별 무역 통계를 조회합니다.")
    @GetMapping(value = "/grouped/{hsSgn}/{cntyCd}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<GroupedTradeDataDTO> getGroupedData(
            @Parameter(description = "HS코드 10자리", required = true, example = "8542310000") @PathVariable String hsSgn,
            @Parameter(description = "국가코드 2자리 (ISO Alpha-2)", required = true, example = "US") @PathVariable String cntyCd
    ) {
        GroupedTradeDataDTO result = tradeApiService.fetchGroupedTradeData(hsSgn, cntyCd);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "HS코드별 Top3 국가 통계 (+지정 국가)", description = "특정 HS코드의 Top3 통계를 조회합니다. 선택적으로 특정 국가(cntyCd)를 지정하면 결과에 함께 포함하여 반환합니다.")
    @GetMapping(value = "/top3/{hsSgn}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<TradeTop3ResultDTO> getTop3TradeStats(
            @Parameter(description = "HS코드 10자리", required = true, example = "8542310000") @PathVariable String hsSgn,
            @Parameter(description = "결과에 함께 포함할 국가코드 (선택 사항)", example = "JP") @RequestParam(required = false) String cntyCd,
            @Parameter(description = "저장할 HS 품목 해설 (조회 이력용)", required = true) @RequestParam String korePrnm,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = (userDetails != null) ? userDetails.getUser().getId() : null;
        TradeTop3ResultDTO result = tradeApiService.fetchTop3TradeStats(hsSgn, cntyCd, korePrnm, userId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "공유 URL 생성", description = "HS코드, 국가코드, 품목명을 받아 공유 가능한 URL을 생성합니다.")
    @GetMapping("/share")
    public ResponseEntity<String> createShareUrl(
            @Parameter(description = "HS코드 10자리", required = true) @RequestParam String hsSgn,
            @Parameter(description = "국가코드 2자리") @RequestParam(required = false) String cntyCd,
            @Parameter(description = "HS 품목 해설") @RequestParam String korePrnm
    ) {
        String shareUrl = UriComponentsBuilder.fromPath("/top3/{hsSgn}")
                .queryParam("cntyCd", cntyCd)
                .queryParam("korePrnm", korePrnm)
                .buildAndExpand(hsSgn)
                .toUriString();
        return ResponseEntity.ok(shareUrl);
    }
}