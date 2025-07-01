
package com.ottrade.ottrade.domain.community.repository;

import com.ottrade.ottrade.domain.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp; // Timestamp 임포트 추가

import java.util.List;

@org.springframework.stereotype.Repository
public interface BoardRepository extends JpaRepository<Post, Long> {

    public List<Post> findByType(String type);

    /**
     * 특정 날짜 이후에 작성된 게시글들을 조회수(view_count)가 높은 순으로 상위 10개 조회
     * @param date 기준 날짜 (예: 7일 전)
     * @return 조회수 높은 상위 10개 게시글 리스트
     */
    List<Post> findTop10ByCreatedAtAfterOrderByViewCountDesc(Timestamp date);

    /**
     * 제목 또는 내용에 특정 키워드가 포함된 게시글 목록 조회
     * @param titleKeyword 제목에서 찾을 키워드
     * @param contentKeyword 내용에서 찾을 키워드
     * @return 검색된 게시글 리스트
     */
    List<Post> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    /**
     * 게시글을 작성한 중복되지 않는 사용자의 총 수를 조회
     */
    @Query("SELECT COUNT(DISTINCT b.userId) FROM Post b")
    long countDistinctUsers();
}