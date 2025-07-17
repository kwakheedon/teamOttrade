package com.ottrade.ottrade.domain.community.entity; // (패키지는 알맞게 수정해주세요)

import java.sql.Timestamp;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Immutable // 읽기 전용 VIEW MAPPING
@Table(name = "my_posts")
public class MyPosts {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private Long userId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 10)
    private String type;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(length = 8)
    private String status;
}