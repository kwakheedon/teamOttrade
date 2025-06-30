package com.ottrade.ottrade.domain.community.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ottrade.ottrade.domain.community.entity.PostLike;
import com.ottrade.ottrade.domain.community.entity.PostLikeId;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    // 'boardId'가 아닌 'post'의 'id'를 참조하도록 메서드 이름 변경
    long countByPostId(Long postId);

    void deleteAllByPostId(Long postId);

    // findBy + {Id필드명} + _ + {Id필드 안의 postId 필드명} + And + {Id필드명} + _ + {Id필드 안의 userId 필드명}
    Optional<PostLike> findById_PostIdAndId_UserId(Long postId, Long userId);

    // deleteBy + {Id필드명} + _ + {Id필드 안의 postId 필드명} + And + {Id필드명} + _ + {Id필드 안의 userId 필드명}
    void deleteById_PostIdAndId_UserId(Long postId, Long userId);
}