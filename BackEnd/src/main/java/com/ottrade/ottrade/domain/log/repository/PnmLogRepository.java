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

    // 가장 많이 검색된 품목명
    @Query("SELECT p.pnm, COUNT(p.pnm) " +
            "FROM PnmLog p " +
            "GROUP BY p.pnm " +
            "ORDER BY COUNT(p.pnm) DESC")
    List<Object[]> findTopKeywords(Pageable pageable);
}