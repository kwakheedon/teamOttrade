package com.ottrade.ottrade.domain.log.service;

import com.ottrade.ottrade.domain.hssearch.dto.TradeTop3ResultDTO;
import com.ottrade.ottrade.domain.hssearch.service.TradeApiService;
import com.ottrade.ottrade.domain.log.dto.SearchLogResponseDTO;
import com.ottrade.ottrade.domain.log.dto.TopSearchKeywordDTO;
import com.ottrade.ottrade.domain.log.entity.PnmLog;
import com.ottrade.ottrade.domain.log.entity.SearchLog;
import com.ottrade.ottrade.domain.log.repository.PnmLogRepository;
import com.ottrade.ottrade.domain.log.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LogService {

    private final PnmLogRepository pnmLogRepository;
    private final SearchLogRepository searchLogRepository;
    private final TradeApiService tradeApiService; // TradeApiService 주입

    public void savePnmLog(String prnm) {
        PnmLog pnmLog = new PnmLog();
        pnmLog.setPnm(prnm);
        pnmLogRepository.save(pnmLog);
    }

    public void saveHsCodeSearchLog(String hsCode, Long userId) {
        SearchLog searchLog = new SearchLog();
        searchLog.setUserId(userId);
        searchLog.setKeyword(hsCode);
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

    /**
     *인기 검색어 Top 10 조회 서비스
     * @return 인기 검색어 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<TopSearchKeywordDTO> getTop10SearchKeywords() {
        // 올바른 클래스를 import하면 (Pageable) 형변환이 필요 없습니다.
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