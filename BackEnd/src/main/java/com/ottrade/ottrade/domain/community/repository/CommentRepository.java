package com.ottrade.ottrade.domain.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ottrade.ottrade.domain.community.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long boardId);

    void deleteAllByPostId(Long boardId);
}
