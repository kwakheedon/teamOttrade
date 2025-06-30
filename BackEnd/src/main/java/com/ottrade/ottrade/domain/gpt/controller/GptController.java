package com.ottrade.ottrade.domain.gpt.controller;

import com.ottrade.ottrade.domain.gpt.service.GptService;
import com.ottrade.ottrade.global.util.ApiResponse;
import com.ottrade.ottrade.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/gpt")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;

    @PostMapping("/analyze/{hsCode}")
    public ResponseEntity<ApiResponse<Map<String, String>>> analyze(
            @PathVariable String hsCode,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 사용자 ID를 추출합니다. 로그인하지 않았으면 null이 됩니다.
        Long userId = (userDetails != null) ? userDetails.getUser().getId() : null;

        Map<String, String> analysisResult = gptService.analyzeHsCode(hsCode, userId);

        return ResponseEntity.ok(ApiResponse.success("AI 분석이 완료되었습니다.", analysisResult));
    }
}