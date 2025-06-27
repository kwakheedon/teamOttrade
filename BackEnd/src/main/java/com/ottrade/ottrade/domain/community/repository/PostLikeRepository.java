package com.ottrade.ottrade.domain.community.repository;

import com.ottrade.ottrade.domain.community.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    long countByBoardId(Long boardId);
}
