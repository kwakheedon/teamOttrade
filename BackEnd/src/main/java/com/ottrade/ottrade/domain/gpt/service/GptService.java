package com.ottrade.ottrade.domain.gpt.service;

import com.ottrade.ottrade.domain.hssearch.dto.TradeTop3ResultDTO;
import com.ottrade.ottrade.domain.hssearch.service.TradeApiService;
import com.ottrade.ottrade.domain.log.entity.SearchLog;
import com.ottrade.ottrade.domain.log.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GptService {

    private final TradeApiService tradeApiService;
    private final SearchLogRepository searchLogRepository;

    /**
     * HS 코드를 분석하여 유망 국가와 사유를 생성하고, 결과를 DB에 저장합니다.
     * @param hsCode 분석할 HS 코드
     * @param userId 요청한 사용자의 ID (비로그인 시 null)
     * @return 분석 결과 (유망 국가, 추천 사유 등)
     */
    @Transactional
    public Map<String, String> analyzeHsCode(String hsCode, @Nullable Long userId) {
        // 1. TradeApiService를 통해 HS코드의 무역 데이터를 가져옵니다.
        TradeTop3ResultDTO tradeData = tradeApiService.fetchTop3TradeStats(hsCode);

        // 2. 무역 데이터를 기반으로 AI 분석 요약문을 생성합니다.
        String summary = generateAnalysisSummary(tradeData);
        String promisingCountry = findPromisingCountry(tradeData);

        // 3. 로그인한 사용자일 경우에만 분석 결과를 DB에 저장합니다.
        if (userId != null) {
            SearchLog searchLog = searchLogRepository.findByUserIdAndKeyword(userId, hsCode)
                    .orElse(new SearchLog());

            searchLog.setUserId(userId);
            searchLog.setKeyword(hsCode);
            searchLog.setGptSummary(summary);
            searchLogRepository.save(searchLog);
        }

        // 4. 컨트롤러에 반환할 결과를 Map 형태로 만듭니다.
        return Map.of(
                "promisingCountry", promisingCountry,
                "reason", summary
        );
    }

    // (이하 다른 메소드들은 변경 없음)
    // ...
    private String findPromisingCountry(TradeTop3ResultDTO data) {
        if (data == null || data.getTopExpDlr().isEmpty()) {
            return "데이터 부족";
        }
        return data.getTopExpDlr().get(0).getStatKor();
    }

    private String generateAnalysisSummary(TradeTop3ResultDTO data) {
        if (data == null || data.getTopExpDlr().isEmpty() || data.getTopImpDlr().isEmpty()) {
            return "분석할 무역 데이터가 부족하여 상세한 추천 사유를 제공하기 어렵습니다.";
        }
        String topExportCountry = data.getTopExpDlr().get(0).getStatKor();
        long topExportValue = data.getTopExpDlr().get(0).getExpDlr();
        String topImportCountry = data.getTopImpDlr().get(0).getStatKor();

        return String.format(
                "해당 품목은 대한민국에서 '%s'(으)로의 수출이 연간 $%d로 가장 활발합니다. 이는 '%s' 시장에서 해당 품목에 대한 수요가 매우 높다는 것을 의미합니다. 반면, '%s'(으)로부터의 수입 의존도가 높아, 안정적인 공급망 관리가 필요합니다. 따라서 현재 가장 수요가 검증된 '%s' 시장 진출을 최우선으로 고려하는 것이 유망합니다.",
                topExportCountry, topExportValue, topExportCountry, topImportCountry, topExportCountry
        );
    }
}