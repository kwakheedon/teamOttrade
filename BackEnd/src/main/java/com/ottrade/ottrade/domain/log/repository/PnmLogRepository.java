package com.ottrade.ottrade.domain.log.repository;

import com.ottrade.ottrade.domain.log.dto.TopSearchKeywordDTO;
import com.ottrade.ottrade.domain.log.entity.PnmLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface PnmLogRepository extends JpaRepository<PnmLog, Long> {
    /**
     * 가장 많이 검색된 품목명(pnm) Top 10을 조회하는 쿼리
     * @param pageable Top 10을 지정하기 위한 페이징 정보
     * @return Top 10 검색어와 횟수가 담긴 DTO 리스트
     */
    @Query("SELECT p.pnm, COUNT(p.pnm) " +
            "FROM PnmLog p " +
            "GROUP BY p.pnm " +
            "ORDER BY COUNT(p.pnm) DESC")
    List<Object[]> findTopKeywords(Pageable pageable);
}