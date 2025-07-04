package com.ottrade.ottrade.domain.community.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository; // @Repository 어노테이션 임포트

import com.ottrade.ottrade.domain.community.entity.Post;

@Repository // @Repository 어노테이션 추가
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByType(String type, Pageable pageable);

    List<Post> findTop10ByCreatedAtAfterOrderByViewCountDesc(LocalDateTime date);

    Page<Post> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword, Pageable pageable);

    /**
     * 게시글을 작성한 중복되지 않는 사용자의 총 수를 조회
     * JPQL은 DB 컬럼명이 아닌 엔티티 필드명을 사용해야 합니다. (b.user_id -> p.userId)
     */
    @Query("SELECT COUNT(DISTINCT p.userId) FROM Post p")
    long countDistinctUsers();

	void deleteAllByUserId(Long userId);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.comments c " +
            "LEFT JOIN FETCH c.user " +
            "WHERE p.id = :postId")
    Optional<Post> findByIdWithDetails(@Param("postId") Long postId);

}
