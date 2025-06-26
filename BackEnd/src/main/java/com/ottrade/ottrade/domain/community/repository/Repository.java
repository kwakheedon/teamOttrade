package com.ottrade.ottrade.domain.community.repository;

import com.ottrade.ottrade.domain.community.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Board, Long> {

    public Board findByType(String type);
}
