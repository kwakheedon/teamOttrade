package com.ottrade.ottrade.domain.hssearch.repository;

import com.ottrade.ottrade.domain.hssearch.entity.SearchLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface SearchLogRepository extends JpaRepository<SearchLogEntity, Long> {
}
