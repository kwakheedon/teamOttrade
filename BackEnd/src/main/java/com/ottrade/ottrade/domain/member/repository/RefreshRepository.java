package com.ottrade.ottrade.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ottrade.ottrade.domain.member.entity.RefreshToken;


@Repository
public interface RefreshRepository extends JpaRepository<RefreshToken, Long> {

}
