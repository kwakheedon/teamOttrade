package com.ottrade.ottrade.domain.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

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

    private String keyword; // HS Code

    @Column(name = "kore_prnm", columnDefinition = "TEXT")
    private String korePrnm;

    @CreationTimestamp
    @Column(name = "searched_at")
    private Timestamp searchedAt;

    @Column(name = "gpt_summary", columnDefinition = "TEXT")
    private String gptSummary;
}