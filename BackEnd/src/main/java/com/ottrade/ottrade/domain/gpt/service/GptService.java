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
@Slf4j
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
        TradeTop3ResultDTO tradeData = tradeApiService.fetchTop3TradeStats(hsCode, null);
        String prompt = createAnalysisPrompt(hsCode, tradeData);

        String summary = "AI 분석에 실패했습니다. 잠시 후 다시 시도해주세요."; // 기본 실패 메시지
        try {
            if (gptApiKey == null || gptApiKey.equals("your-actual-gpt-api-key")) {
                throw new IllegalArgumentException("GPT API Key가 설정되지 않았습니다. application-secret.properties 파일을 확인해주세요.");
            }
            summary = callGptApi(prompt);
        } catch (Exception e) {
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gptApiKey);

        GptRequestDto.Message message = GptRequestDto.Message.builder()
                .role("user")
                .content(prompt)
                .build();

        GptRequestDto requestDto = GptRequestDto.builder()
                .model(gptModelName)
                .messages(List.of(message))
                .build();

        HttpEntity<GptRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

        GptResponseDto responseDto = restTemplate.postForObject(gptApiUrl, requestEntity, GptResponseDto.class);

        if (responseDto != null && !responseDto.getChoices().isEmpty()) {
            return responseDto.getChoices().get(0).getMessage().getContent();
        }

        throw new RuntimeException("AI로부터 유효한 응답을 받지 못했습니다.");
    }

    private void saveSearchLog(Long userId, String hsCode, String summary) {
        SearchLog searchLog = searchLogRepository.findTopByUserIdAndKeywordOrderBySearchedAtDesc(userId, hsCode)
                .orElse(new SearchLog());
        searchLog.setUserId(userId);
        searchLog.setKeyword(hsCode);
        searchLog.setGptSummary(summary);
        searchLogRepository.save(searchLog);
    }

    private String findPromisingCountry(TradeTop3ResultDTO data) {
        if (data == null || data.getTopExpDlr().isEmpty()) {
            return "데이터 부족";
        }
        return data.getTopExpDlr().get(0).getStatKor();
    }

    private String createAnalysisPrompt(String hsCode, TradeTop3ResultDTO data) {
        if (data == null) {
            return "분석할 데이터가 없습니다.";
        }

        StringBuilder dataString = new StringBuilder();
        dataString.append("### HS Code: ").append(hsCode).append("\n\n");

        dataString.append("#### 최근 1년간 주요 수출국 Top 3 (금액 기준)\n");
        dataString.append("| 순위 | 국가 | 수출 금액 (USD) |\n");
        dataString.append("|---|---|---|\n");
        int rank = 1;
        for (TradeTopCountryDTO dto : data.getTopExpDlr()) {
            dataString.append(String.format("| %d | %s | $%,d |\n", rank++, dto.getStatKor(), dto.getExpDlr()));
        }

        dataString.append("\n#### 최근 1년간 주요 수입국 Top 3 (금액 기준)\n");
        dataString.append("| 순위 | 국가 | 수입 금액 (USD) |\n");
        dataString.append("|---|---|---|\n");
        rank = 1;
        for (TradeTopCountryDTO dto : data.getTopImpDlr()) {
            dataString.append(String.format("| %d | %s | $%,d |\n", rank++, dto.getStatKor(), dto.getImpDlr()));
        }

        return String.format(
                "당신은 15년 경력의 베테랑 무역 컨설턴트입니다. 아래 제공된 무역 데이터를 바탕으로, 한국의 중소기업이 해당 HS Code 품목을 해외 시장에 성공적으로 진출시키기 위한 구체적인 전략 보고서를 작성해 주십시오.\n\n" +
                        "보고서는 다음 항목을 반드시 포함해야 하며, 전문가의 시각에서 깊이 있는 분석과 실행 가능한 조언을 담아주십시오. 답변은 반드시 한글로, 총 500자 내외로 작성해 주십시오.\n\n" +
                        "**1. 시장 개요:** 해당 품목의 글로벌 시장 동향 및 특징을 간략하게 분석합니다.\n\n" +
                        "**2. 주요 교역국 분석:**\n" +
                        "   - **주요 수출국:** 왜 이 국가들이 주요 수출국인지 그 배경(예: 생산 능력, 기술력)을 추론합니다.\n" +
                        "   - **주요 수입국:** 왜 이 국가들이 주요 수입국인지 그 배경(예: 시장 수요, 자국 생산 부족)을 추론합니다.\n\n" +
                        "**3. 유망 진출 시장 추천 및 근거:**\n" +
                        "   - 데이터를 기반으로 가장 유망하다고 판단되는 국가 1~2개를 추천하고, 그 이유를 구체적인 데이터(수출입 규모, 무역수지, 성장 가능성 등)를 근거로 제시합니다.\n\n" +
                        "**4. 시장 진출 전략 제안:**\n" +
                        "   - 추천한 유망 시장에 진출하기 위한 초기 전략(예: 타겟 고객, 가격 정책, 마케팅 채널, 현지 파트너십)을 제시합니다.\n\n" +
                        "**5. 잠재 리스크 및 유의사항:**\n" +
                        "   - 목표 시장 진출 시 발생할 수 있는 잠재적 리스크(예: 관세/비관세 장벽, 경쟁 심화, 문화적 차이)를 언급하고, 이에 대한 대비책을 간략하게 조언합니다.\n\n" +
                        "--- \n\n" +
                        "**[무역 데이터]**\n" +
                        "%s\n\n" +
                        "--- \n\n" +
                        "**[분석 보고서]**",
                dataString
        );
    }
}