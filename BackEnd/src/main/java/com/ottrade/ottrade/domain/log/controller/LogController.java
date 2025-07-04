package com.ottrade.ottrade.domain.log.controller;

import com.ottrade.ottrade.domain.hssearch.dto.TradeTop3ResultDTO;
import com.ottrade.ottrade.domain.log.dto.SearchLogResponseDTO;
import com.ottrade.ottrade.domain.log.dto.TopSearchKeywordDTO;
import com.ottrade.ottrade.domain.log.service.LogService;
import com.ottrade.ottrade.global.util.ApiResponse;
import com.ottrade.ottrade.security.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Search History", description = "사용자별 검색 이력 조회 API")
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping("/my-history")
    @Operation(summary = "내 검색 이력 조회", description = "인증된 사용자의 HS코드 검색 이력 목록을 최신순으로 조회합니다.")
    public ResponseEntity<ApiResponse<List<SearchLogResponseDTO>>> getMySearchHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<SearchLogResponseDTO> history = logService.getSearchHistory(userDetails.getUser().getId());
        return ResponseEntity.ok(ApiResponse.success("내 검색 이력 조회 성공", history));
    }

    @GetMapping("/my-history/{logId}")
    @Operation(summary = "특정 검색 이력 상세 조회", description = "특정 검색 이력 ID에 해당하는 과거 시점의 상세 데이터를 조회합니다.")
    public ResponseEntity<ApiResponse<TradeTop3ResultDTO>> getSearchLogDetail(
            @Parameter(description = "조회할 검색 이력의 ID") @PathVariable Long logId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        TradeTop3ResultDTO result = logService.getSearchLogDetail(logId);
        return ResponseEntity.ok(ApiResponse.success("검색 이력 상세 조회 성공", result));
    }

    @GetMapping("/top-keywords")
    @Operation(summary = "인기 검색어 Top 10 조회", description = "사용자들이 가장 많이 검색한 품목명 순위 10개를 조회합니다.")
    public ResponseEntity<ApiResponse<List<TopSearchKeywordDTO>>> getTopSearchKeywords() {
        List<TopSearchKeywordDTO> topKeywords = logService.getTop10SearchKeywords();
        return ResponseEntity.ok(ApiResponse.success("인기 검색어 조회 성공", topKeywords));
    }

    @DeleteMapping("/my-history/{logId}")
    @Operation(summary = "조회이력 삭제", description = "인증된 사용자의 HS코드 검색 특정 이력 삭제")
    public ResponseEntity<ApiResponse<Void>> delSearchHistory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                              @PathVariable Long logId
                                                              ) {
        logService.deleteSearchHistory(logId);
        return ResponseEntity.ok(ApiResponse.success("내검색이력 삭제 성공"));
    }
}