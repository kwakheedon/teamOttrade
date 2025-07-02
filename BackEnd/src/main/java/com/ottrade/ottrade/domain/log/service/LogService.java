package com.ottrade.ottrade.domain.log.service;

import com.ottrade.ottrade.domain.hssearch.dto.TradeTop3ResultDTO;
import com.ottrade.ottrade.domain.hssearch.service.TradeApiService;
import com.ottrade.ottrade.domain.log.dto.SearchLogResponseDTO;
import com.ottrade.ottrade.domain.log.dto.TopSearchKeywordDTO;
import com.ottrade.ottrade.domain.log.entity.PnmLog;
import com.ottrade.ottrade.domain.log.entity.SearchLog;
import com.ottrade.ottrade.domain.log.repository.PnmLogRepository;
import com.ottrade.ottrade.domain.log.repository.SearchLogRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LogService {

    private final PnmLogRepository pnmLogRepository;
    private final SearchLogRepository searchLogRepository;
    private final TradeApiService tradeApiService;

    public LogService(PnmLogRepository pnmLogRepository, SearchLogRepository searchLogRepository, @Lazy TradeApiService tradeApiService) {
        this.pnmLogRepository = pnmLogRepository;
        this.searchLogRepository = searchLogRepository;
        this.tradeApiService = tradeApiService;
    }

    public void savePnmLog(String prnm) {
        PnmLog pnmLog = new PnmLog();
        pnmLog.setPnm(prnm);
        pnmLogRepository.save(pnmLog);
    }

    public void saveHsCodeSearchLog(String hsCode, Long userId, String korePrnm) {
        SearchLog searchLog = new SearchLog();
        searchLog.setUserId(userId);
        searchLog.setKeyword(hsCode);
        searchLog.setKorePrnm(korePrnm); // HS 품목 해설 저장
        searchLogRepository.save(searchLog);
    }

    @Transactional(readOnly = true)
    public List<SearchLogResponseDTO> getSearchHistory(Long userId) {
        return searchLogRepository.findAllByUserIdOrderBySearchedAtDesc(userId)
                .stream()
                .map(SearchLogResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TradeTop3ResultDTO getSearchLogDetail(Long logId) {
        SearchLog log = searchLogRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 검색 이력입니다."));

        LocalDateTime searchedAt = log.getSearchedAt().toLocalDateTime();
        return tradeApiService.fetchTradeStatsByLog(log.getKeyword(), null, searchedAt);
    }

    @Transactional(readOnly = true)
    public List<TopSearchKeywordDTO> getTop10SearchKeywords() {
        Pageable topTen = PageRequest.of(0, 10);
        List<Object[]> results = pnmLogRepository.findTopKeywords(topTen);

        return results.stream()
                .map(result -> new TopSearchKeywordDTO(
                        (String) result[0],
                        ((Number) result[1]).longValue()
                ))
                .collect(Collectors.toList());
    }
}