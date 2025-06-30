package com.ottrade.ottrade.domain.log.repository;

import com.ottrade.ottrade.domain.log.entity.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {
}