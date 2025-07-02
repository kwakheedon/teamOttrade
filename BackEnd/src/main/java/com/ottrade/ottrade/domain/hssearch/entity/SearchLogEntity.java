package com.ottrade.ottrade.domain.hssearch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "search_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String keyword;

    @Column(name = "searched_at", columnDefinition = "DATETIME DEFAULT NOW()")
    private String searched_at;

    @Column(name = "gpt_summary", columnDefinition = "TEXT")
    private String gpt_summary;
}