
package com.ottrade.ottrade.domain.community.repository;

import com.ottrade.ottrade.domain.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp; // Timestamp 임포트 추가

import java.util.List;

@org.springframework.stereotype.Repository
public interface BoardRepository extends JpaRepository<Post, Long> {

    public List<Post> findByType(String type);

    // viewcount 기준 상위 10개
    List<Post> findTop10ByCreatedAtAfterOrderByViewCountDesc(Timestamp date);

    // 특정 키워드가 포함된 게시글조회
    List<Post> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    // 사용자 총수 조회
    @Query("SELECT COUNT(DISTINCT b.userId) FROM Post b")
    long countDistinctUsers();
}