package com.ottrade.ottrade.domain.gpt.controller;

import com.ottrade.ottrade.domain.gpt.service.GptService;
import com.ottrade.ottrade.global.util.ApiResponse;
import com.ottrade.ottrade.security.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "GPT AI Analysis", description = "HS코드 관련 AI 분석 API")
@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;

    @Operation(summary = "HS코드 AI 분석", description = "HS코드에 대한 무역 통계 데이터를 기반으로 AI가 시장 진출 전략과 유망 국가를 분석합니다.")
    @PostMapping("/analyze/{hsCode}")
    public ResponseEntity<ApiResponse<Map<String, String>>> analyze(
            @Parameter(description = "분석할 HS코드 10자리", required = true, example = "8542310000") @PathVariable String hsCode,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = (userDetails != null) ? userDetails.getUser().getId() : null;

        Map<String, String> analysisResult = gptService.analyzeHsCode(hsCode, userId);

        return ResponseEntity.ok(ApiResponse.success("AI 분석이 완료되었습니다.", analysisResult));
    }
}