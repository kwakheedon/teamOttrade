package com.ottrade.ottrade.domain.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "post_like")
public class PostLike {
    @EmbeddedId
    private PostLikeId id;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // Board -> Post로 참조 변경

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt; // ERD에 맞게 created_at 추가
}