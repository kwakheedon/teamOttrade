package com.ottrade.ottrade.domain.log.entity; // (패키지는 알맞게 수정해주세요)

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "search_log")
public class SearchLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String keyword;

    @CreationTimestamp
    @Column(name = "searched_at")
    private Timestamp searchedAt;

    @Column(name = "gpt_summary", columnDefinition = "TEXT")
    private String gptSummary;
}