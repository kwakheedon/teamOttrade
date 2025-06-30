package com.ottrade.ottrade.domain.community.repository;

import com.ottrade.ottrade.domain.community.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    long countByBoardId(Long boardId);

    void deleteAllByBoardId(Long boardId);

    // findBy + {Id필드명} + _ + {Id필드 안의 postId 필드명} + And + {Id필드명} + _ + {Id필드 안의 userId 필드명}
    Optional<PostLike> findById_PostIdAndId_UserId(Long postId, Long userId);

    // deleteBy + {Id필드명} + _ + {Id필드 안의 postId 필드명} + And + {Id필드명} + _ + {Id필드 안의 userId 필드명}
    void deleteById_PostIdAndId_UserId(Long postId, Long userId);
}
