package com.ottrade.ottrade.domain.community.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.CascadeType; // CascadeType 임포트 추가

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id" , nullable = false)
    private Long user_id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String type;

    // --- 아래 필드 2개를 수정합니다. ---
    @ColumnDefault("0")
    private int viewCount; // view_count -> viewCount 로 변경

    @CreationTimestamp
    private Timestamp createdAt; // created_at -> createdAt 으로 변경

    @ColumnDefault("'ENABLE'")
    @Column(name="status" ,nullable = false)
    private String status;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLike> postLikes = new LinkedHashSet<>();


}
