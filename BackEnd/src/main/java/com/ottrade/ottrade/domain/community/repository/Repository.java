package com.ottrade.ottrade.domain.community.repository;

import com.ottrade.ottrade.domain.community.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Board, Long> {

    public List<Board> findByType(String type);
}
