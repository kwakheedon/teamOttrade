package com.ottrade.ottrade.domain.community.entity;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post") // ERD의 post 테이블과 매핑
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id") // 명시적으로 컬럼명 지정
    private Long userId;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(length = 10, nullable = false)
    private String type;

    @Column(name = "view_count")
    @ColumnDefault("0")
    private int viewCount;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(length = 8)
    private String status;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLike> postLikes = new LinkedHashSet<>();

}