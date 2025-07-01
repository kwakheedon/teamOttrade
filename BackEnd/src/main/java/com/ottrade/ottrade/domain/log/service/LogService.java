package com.ottrade.ottrade.domain.log.service;

import com.ottrade.ottrade.domain.log.entity.PnmLog;
import com.ottrade.ottrade.domain.log.entity.SearchLog; // SearchLog 임포트
import com.ottrade.ottrade.domain.log.repository.PnmLogRepository;
import com.ottrade.ottrade.domain.log.repository.SearchLogRepository; // SearchLogRepository 임포트
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LogService {

    private final PnmLogRepository pnmLogRepository;
    private final SearchLogRepository searchLogRepository; // 주석 해제

    /**
     * 품목명(prnm) 검색 기록 저장
     * @param prnm 검색한 품목명
     */
    public void savePnmLog(String prnm) {
        PnmLog pnmLog = new PnmLog();
        pnmLog.setPnm(prnm);
        pnmLogRepository.save(pnmLog);
    }

    /**
     * HS코드 검색 기록 저장 (구현)
     * @param hsCode 검색한 HS코드
     * @param userId 검색한 사용자의 ID
     */
    public void saveHsCodeSearchLog(String hsCode, Long userId) {
        // 이전에 같은 검색을 했는지 확인하지 않고, 항상 새로운 로그로 저장합니다.
        // 만약 중복 저장을 막고 싶다면 findByUserIdAndKeyword 로직을 추가할 수 있습니다.
        SearchLog searchLog = new SearchLog();
        searchLog.setUserId(userId);
        searchLog.setKeyword(hsCode);
        // gptSummary는 AI 분석 시점에 채워지므로 여기서는 비워둡니다.
        searchLogRepository.save(searchLog);
    }
}