package com.ottrade.ottrade.domain.log.service;

import com.ottrade.ottrade.domain.log.entity.PnmLog;
import com.ottrade.ottrade.domain.log.repository.PnmLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LogService {

    private final PnmLogRepository pnmLogRepository;
    // private final SearchLogRepository searchLogRepository; // 추후 HS코드 로그 저장 시 사용

    /**
     * 품목명(prnm) 검색 기록 저장
     * @param prnm 검색한 품목명
     */
    public void savePnmLog(String prnm) {
        PnmLog pnmLog = new PnmLog();
        pnmLog.setPnm(prnm);
        pnmLogRepository.save(pnmLog);
    }

    // TODO: 추후 HS코드 및 사용자 ID를 저장하는 로직 추가
    /*
    public void saveHsCodeSearchLog(String hsCode, Long userId) {
        SearchLog searchLog = new SearchLog();
        searchLog.setKeyword(hsCode);
        searchLog.setUserId(userId);
        // gptSummary는 AI 기능 구현 후 추가
        searchLogRepository.save(searchLog);
    }
    */
}