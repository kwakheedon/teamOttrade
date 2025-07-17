package com.ottrade.ottrade.domain.community.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ottrade.ottrade.domain.community.entity.PostLike;
import com.ottrade.ottrade.domain.community.entity.PostLikeId;

import jakarta.transaction.Transactional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {

    long countByPostId(Long postId);

    void deleteAllByPostId(Long postId);

    Optional<PostLike> findById_PostIdAndId_UserId(Long postId, Long userId);

    void deleteById_PostIdAndId_UserId(Long postId, Long userId);

    

	 @Transactional
     void deleteAllByUser_Id(Long userId);
     void deleteById_UserId(Long userId);
     void deleteAllByUserId(Long userId);

}