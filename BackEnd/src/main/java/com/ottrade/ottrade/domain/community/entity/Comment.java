package com.ottrade.ottrade.domain.community.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "comment", schema = "springuser")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Board post;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "status", nullable = false)
    private String status;

    // --- 대댓글 기능을 위한 필드 추가 ---

    // 부모 댓글 (ManyToOne 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 자식 댓글 목록 (OneToMany 관계)
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    //TODO [Reverse Engineering] generate columns from DB
}