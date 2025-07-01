package com.ottrade.ottrade.domain.community.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.ottrade.ottrade.domain.member.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "post_like")
public class PostLike {
    @EmbeddedId
    private PostLikeId id;
    
  
    // PostLikeId의 userId 부분을 User 엔티티와 매핑합니다.
    @MapsId("userId") // PostLikeId의 'userId' 필드를 이 @ManyToOne 관계의 ID로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false) // user_id 컬럼을 조인하고, 삽입/업데이트는 MapsId에 맡김
    private User user; // <-- User 엔티티를 직접 참조하는 필드 추가!
    

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // Board -> Post로 참조 변경

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt; // ERD에 맞게 created_at 추가
}