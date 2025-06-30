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
@RequestMapping("/api/gpt") // API 주소를 /api/gpt로 변경합니다.
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;

    @PostMapping("/analyze/{hsCode}")
    public ResponseEntity<ApiResponse<Map<String, String>>> analyze(
            @PathVariable String hsCode,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("AI 분석 기능은 로그인이 필요합니다."));
        }

        Map<String, String> analysisResult = gptService.analyzeHsCode(hsCode, userDetails.getUser().getId());

        return ResponseEntity.ok(ApiResponse.success("AI 분석이 완료되었습니다.", analysisResult));
    }
}