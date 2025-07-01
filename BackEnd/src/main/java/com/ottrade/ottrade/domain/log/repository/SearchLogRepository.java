package com.ottrade.ottrade.domain.log.repository;

import com.ottrade.ottrade.domain.log.entity.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Optional 임포트

@Repository
public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {

    // 사용자 ID와 키워드로 로그를 찾는 메소드 추가
    Optional<SearchLog> findByUserIdAndKeyword(Long userId, String keyword);
    // 사용자의 모든 검색 기록을 최신순으로 조회하는 메소드 추가
    List<SearchLog> findAllByUserIdOrderBySearchedAtDesc(Long userId);
}