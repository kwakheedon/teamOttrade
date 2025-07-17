package com.ottrade.ottrade.domain.community.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ottrade.ottrade.domain.community.repository.BoardRepository;
import com.ottrade.ottrade.domain.member.repository.UserRepository;
import com.ottrade.ottrade.security.user.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ottrade.ottrade.domain.community.dto.AllBoardRespDTO;
import com.ottrade.ottrade.domain.community.dto.BoardDetailRespDTO;
import com.ottrade.ottrade.domain.community.dto.BoardUpdateReqDTO;
import com.ottrade.ottrade.domain.community.dto.BoardWriteDTO;
import com.ottrade.ottrade.domain.community.dto.CommentCreateRequest;
import com.ottrade.ottrade.domain.community.dto.CommentDTO;
import com.ottrade.ottrade.domain.community.dto.TotalStatsDTO; // TotalStatsDTO 임포트
import com.ottrade.ottrade.domain.community.entity.Comment;
import com.ottrade.ottrade.domain.community.entity.Post;
import com.ottrade.ottrade.domain.community.entity.PostLike;
import com.ottrade.ottrade.domain.community.entity.PostLikeId;
import com.ottrade.ottrade.domain.community.repository.CommentRepository;
import com.ottrade.ottrade.domain.community.repository.PostLikeRepository;
import com.ottrade.ottrade.domain.community.repository.PostRepository;
import com.ottrade.ottrade.domain.member.entity.User; // User 엔티티 import 추가

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    @Transactional
    public String boardWrite(BoardWriteDTO boardWriteDTO, CustomUserDetails userDetails) {
        Post post = new Post();
        post.setUserId(userDetails.getUser().getId());
        post.setTitle(boardWriteDTO.getTitle());
        post.setContent(boardWriteDTO.getContent());
        post.setType(boardWriteDTO.getType());
        post.setStatus("enable");
        postRepository.save(post);
        return "성공";
    }

    public Page<AllBoardRespDTO> allBoard(String type, Pageable pageable) {
        Page<Post> postPage = postRepository.findByType(type, pageable);

        return postPage.map(AllBoardRespDTO::fromEntity);
    }

    @Transactional
    public Post updateBoard(BoardUpdateReqDTO boardUpdateReqDTO, Long currentUserId) {
        Post postUpdate = postRepository.findById(boardUpdateReqDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID " + boardUpdateReqDTO.getId() + "에 해당하는 게시글을 찾을 수 없습니다."));

        if (!postUpdate.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("게시글을 수정할 권한이 없습니다.");
        }

        postUpdate.setTitle(boardUpdateReqDTO.getTitle());
        postUpdate.setContent(boardUpdateReqDTO.getContent());

        return postUpdate;
    }

    @Transactional
    public void deleteBoard(Long boardId, Long currentUserId) {
        Post post = postRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("ID " + boardId + "에 해당하는 게시글을 찾을 수 없습니다."));

        if (!post.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("게시글을 삭제할 권한이 없습니다.");
        }

        commentRepository.deleteAllByPostId(boardId);
        postLikeRepository.deleteAllByPostId(boardId);

        postRepository.deleteById(boardId);
    }

    @Transactional
    public BoardDetailRespDTO detailBoard(Long boardId, CustomUserDetails userDetails) {
        Post post = postRepository.findByIdWithDetails(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        post.setViewCount(post.getViewCount() + 1);

        List<CommentDTO> commentDTOs = post.getComments().stream()
                .filter(c -> c.getParent() == null)
                .map(CommentDTO::new)
                .collect(Collectors.toList());

        long likeCount = postLikeRepository.countByPostId(boardId);
        boolean isLiked = false;

        if (userDetails != null) {
            Long userId = userDetails.getUser().getId();
            isLiked = postLikeRepository.findById_PostIdAndId_UserId(boardId, userId).isPresent();
        }

        String authorNickname = (post.getUser() != null) ? post.getUser().getNickname() : "알 수 없음";

        return new BoardDetailRespDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUserId(),
                authorNickname,
                post.getCreatedAt(),
                commentDTOs,
                (int) likeCount,
                isLiked
        );
    }

    @Transactional
    public CommentDTO createComment(Long boardId, CommentCreateRequest request, Long userId) {

        Post post = postRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        comment.setContent(request.getContent());
        comment.setStatus(Comment.Status.valueOf("enable"));
        comment.setPost(post);
        comment.setUserId(userId);

        if (request.getParentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다."));
            comment.setParent(parentComment);
        }

        commentRepository.save(comment);

        return new CommentDTO(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("ID " + commentId + "에 해당하는 댓글을 찾을 수 없습니다."));

        if (!comment.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("댓글을 삭제할 권한이 없습니다.");
        }

        if (!comment.getChildren().isEmpty()) {
            comment.setContent("삭제된 댓글입니다.");
            comment.setStatus(Comment.Status.valueOf("disable"));
        } else {
            commentRepository.delete(comment);
        }
    }


    @Transactional
    public void addLike(Long boardId, Long userId) {
        Post post = postRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Optional<PostLike> existingLike = postLikeRepository.findById_PostIdAndId_UserId(boardId, userId);
        if (existingLike.isPresent()) {
            throw new IllegalStateException("이미 좋아요를 누른 게시글입니다.");
        }

        PostLikeId likeId = new PostLikeId();
        likeId.setPostId(boardId);
        likeId.setUserId(userId);

        PostLike newLike = new PostLike();
        newLike.setId(likeId);
        newLike.setPost(post);

        newLike.setUser(user); // User 엔티티를 설정합니다.

        postLikeRepository.save(newLike);
    }

    @Transactional
    public void removeLike(Long boardId, Long userId) {
        Optional<PostLike> existingLike = postLikeRepository.findById_PostIdAndId_UserId(boardId, userId);
        if (existingLike.isEmpty()) {
            throw new IllegalArgumentException("좋아요를 누르지 않은 게시글입니다.");
        }

        postLikeRepository.deleteById_PostIdAndId_UserId(boardId, userId);
    }

    @Transactional(readOnly = true)
    public List<AllBoardRespDTO> getHotBoards() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        List<Post> hotPostList = postRepository.findTop10ByCreatedAtAfterOrderByViewCountDesc(sevenDaysAgo);

        return hotPostList.stream()
                .map(AllBoardRespDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<AllBoardRespDTO> searchBoards(String keyword, Pageable pageable) {
        Page<Post> searchResultPage = postRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);

        return searchResultPage.map(AllBoardRespDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public TotalStatsDTO getTotalStats() {
        long totalPosts = postRepository.count();

        long totalUsers = userRepository.count();

        return new TotalStatsDTO(totalUsers, totalPosts);
    }
}