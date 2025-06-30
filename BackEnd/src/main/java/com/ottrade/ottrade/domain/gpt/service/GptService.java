package com.ottrade.ottrade.domain.gpt.service;

import com.ottrade.ottrade.domain.gpt.dto.GptRequestDto;
import com.ottrade.ottrade.domain.gpt.dto.GptResponseDto;
import com.ottrade.ottrade.domain.hssearch.dto.TradeTop3ResultDTO;
import com.ottrade.ottrade.domain.hssearch.dto.TradeTopCountryDTO;
import com.ottrade.ottrade.domain.hssearch.service.TradeApiService;
import com.ottrade.ottrade.domain.log.entity.SearchLog;
import com.ottrade.ottrade.domain.log.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Slf4j 로거 임포트
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j // 로거 사용을 위한 어노테이션 추가
public class GptService {

    private final TradeApiService tradeApiService;
    private final SearchLogRepository searchLogRepository;
    private final RestTemplate restTemplate;

    @Value("${gpt.api.url}")
    private String gptApiUrl;

    @Value("${gpt.model.name}")
    private String gptModelName;

    @Value("${GPT_API_KEY}")
    private String gptApiKey;


    @Transactional
    public Map<String, String> analyzeHsCode(String hsCode, @Nullable Long userId) {
        TradeTop3ResultDTO tradeData = tradeApiService.fetchTop3TradeStats(hsCode);
        String prompt = createAnalysisPrompt(hsCode, tradeData);

        String summary = "AI 분석에 실패했습니다. 잠시 후 다시 시도해주세요."; // 기본 실패 메시지
        try {
            // gptApiKey가 예시 값 그대로인지 확인
            if (gptApiKey == null || gptApiKey.equals("your-actual-gpt-api-key")) {
                throw new IllegalArgumentException("GPT API Key가 설정되지 않았습니다. application-secret.properties 파일을 확인해주세요.");
            }
            summary = callGptApi(prompt);
        } catch (Exception e) {
            // API 호출 실패 시 정확한 에러 로그를 남깁니다.
            log.error("GPT API 호출 중 오류 발생: {}", e.getMessage(), e);
        }

        if (userId != null) {
            saveSearchLog(userId, hsCode, summary);
        }

        return Map.of(
                "promisingCountry", findPromisingCountry(tradeData),
                "reason", summary
        );
    }

    private String callGptApi(String prompt) {
        // 1. HTTP 헤더 설정 (API Key 포함)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gptApiKey);

        // 2. API 요청 본문(Body) 생성
        GptRequestDto.Message message = GptRequestDto.Message.builder()
                .role("user")
                .content(prompt)
                .build();

        GptRequestDto requestDto = GptRequestDto.builder()
                .model(gptModelName)
                .messages(List.of(message))
                .build();

        // 3. HTTP 요청 엔티티 생성
        HttpEntity<GptRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

        // 4. RestTemplate을 사용하여 API 호출
        GptResponseDto responseDto = restTemplate.postForObject(gptApiUrl, requestEntity, GptResponseDto.class);

        // 5. 응답에서 실제 텍스트 답변 추출
        if (responseDto != null && !responseDto.getChoices().isEmpty()) {
            return responseDto.getChoices().get(0).getMessage().getContent();
        }

        throw new RuntimeException("AI로부터 유효한 응답을 받지 못했습니다.");
    }

    private void saveSearchLog(Long userId, String hsCode, String summary) {
        SearchLog searchLog = searchLogRepository.findByUserIdAndKeyword(userId, hsCode)
                .orElse(new SearchLog());
        searchLog.setUserId(userId);
        searchLog.setKeyword(hsCode);
        searchLog.setGptSummary(summary);
        searchLogRepository.save(searchLog);
    }

    // 유망 국가 찾기 (기존 로직)
    private String findPromisingCountry(TradeTop3ResultDTO data) {
        if (data == null || data.getTopExpDlr().isEmpty()) {
            return "데이터 부족";
        }
        return data.getTopExpDlr().get(0).getStatKor();
    }

    // AI에게 질문할 프롬프트를 생성하는 메소드
    private String createAnalysisPrompt(String hsCode, TradeTop3ResultDTO data) {
        if (data == null) {
            return "분석할 데이터가 없습니다.";
        }
        // 데이터를 문자열로 변환
        StringBuilder dataString = new StringBuilder();
        dataString.append("HS Code: ").append(hsCode).append("\n\n");
        dataString.append("최근 1년간 주요 수출국 Top 3 (금액 기준):\n");
        for (TradeTopCountryDTO dto : data.getTopExpDlr()) {
            dataString.append(String.format("- %s: $%,d\n", dto.getStatKor(), dto.getExpDlr()));
        }
        dataString.append("\n최근 1년간 주요 수입국 Top 3 (금액 기준):\n");
        for (TradeTopCountryDTO dto : data.getTopImpDlr()) {
            dataString.append(String.format("- %s: $%,d\n", dto.getStatKor(), dto.getImpDlr()));
        }

        // AI에게 내리는 최종 지시사항
        return String.format(
                "너는 무역 컨설턴트야. 아래의 무역 데이터를 바탕으로, 해당 HS Code 품목의 해외 시장 진출 전략에 대해 분석하고 유망 국가를 추천해줘. 반드시 친절한 전문가 말투를 사용하고, 답변은 한글로 해줘.\n\n[무역 데이터]\n%s\n\n[분석 결과]",
                dataString
        );
    }
}