package com.ottrade.ottrade.domain.community.service;

import com.ottrade.ottrade.domain.community.dto.*;
import com.ottrade.ottrade.domain.community.entity.Post;
import com.ottrade.ottrade.domain.community.entity.Comment;
import com.ottrade.ottrade.domain.community.repository.CommentRepository;
import com.ottrade.ottrade.domain.community.repository.PostLikeRepository;
import com.ottrade.ottrade.domain.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.ottrade.ottrade.domain.community.entity.PostLike;
import com.ottrade.ottrade.domain.community.entity.PostLikeId;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import com.ottrade.ottrade.domain.community.dto.TotalStatsDTO; // TotalStatsDTO 임포트


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public String boardWrite(BoardWriteDTO boardWriteDTO) {
        Post post = new Post();
        post.setUserId(boardWriteDTO.getUser_id());
        post.setTitle(boardWriteDTO.getTitle());
        post.setContent(boardWriteDTO.getContent());
        post.setType(boardWriteDTO.getType());
        post.setStatus("enable");
        postRepository.save(post);
        return "성공";
    }

    public List<AllBoardRespDTO> allBoard(String type) {
        // 1. Repository를 통해 Board 엔티티 리스트를 조회합니다.
        List<Post> postList = postRepository.findByType(type);

        // 2. Stream API를 사용하여 각 Board 엔티티를 AllBoardRespDTO로 변환합니다.
        //    (fromEntity가 static 메소드라고 가정)
        List<AllBoardRespDTO> dtoList = postList.stream()
                .map(board -> AllBoardRespDTO.fromEntity(board)) // 각 board를 DTO로 매핑
                .collect(Collectors.toList()); // 결과를 List로 수집

        // 3. 변환된 DTO 리스트를 반환합니다.
        return dtoList;
    }

    @Transactional
    public Post updateBoard(BoardUpdateReqDTO boardUpdateReqDTO, Long currentUserId) {
        Post postUpdate = postRepository.findById(boardUpdateReqDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID " + boardUpdateReqDTO.getId() + "에 해당하는 게시글을 찾을 수 없습니다."));

        // 게시글 작성자와 현재 로그인한 사용자가 같은지 확인
        if (!postUpdate.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("게시글을 수정할 권한이 없습니다.");
        }

        postUpdate.setTitle(boardUpdateReqDTO.getTitle());
        postUpdate.setContent(boardUpdateReqDTO.getContent());

        return postUpdate; // 변경 감지로 인해 save 호출 불필요
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        if (!postRepository.existsById(boardId)) {
            throw new IllegalArgumentException("ID " + boardId + "에 해당하는 게시글을 찾을 수 없습니다.");
        }

        // 추가된 로직: 게시글에 연관된 댓글과 좋아요를 먼저 삭제합니다.
        commentRepository.deleteAllByPostId(boardId);
        postLikeRepository.deleteAllByPostId(boardId); // 메서드 이름 수정

        postRepository.deleteById(boardId);
    }

    @Transactional()
    public BoardDetailRespDTO detailBoard(Long boardId) {
        // 1. 게시글 조회
        Post post = postRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 2. 조회수 1 증가
        post.setViewCount(post.getViewCount() + 1);
        // repository.save(board)를 명시적으로 호출할 필요가 없습니다.
        // @Transactional 안에서 엔티티가 변경되면 자동으로 DB에 반영됩니다.

        // 3. 댓글, 좋아요 수 조회
        List<Comment> comments = commentRepository.findByPostId(boardId);
        List<CommentDTO> commentDTOs = comments.stream()
                .filter(c -> c.getParent() == null) // 최상위 댓글만 필터링
                .map(CommentDTO::new)
                .collect(Collectors.toList());
        long likeCount = postLikeRepository.countByPostId(boardId); // 메서드 이름 수정

        // 4. DTO 매핑
        return new BoardDetailRespDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUserId(), // getUserId()로 수정
                commentDTOs,
                (int) likeCount
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

        // --- 대댓글 처리 로직 추가 ---
        if (request.getParentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다."));
            comment.setParent(parentComment);
        }

        commentRepository.save(comment);

        // DTO 변환 로직은 DTO 생성자로 위임
        return new CommentDTO(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        // 삭제할 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("ID " + commentId + "에 해당하는 댓글을 찾을 수 없습니다."));

        // 1. 자식 댓글(대댓글)이 있는지 확인
        if (!comment.getChildren().isEmpty()) {
            // 2. 자식 댓글이 있으면, 내용만 변경 (Soft Delete)
            comment.setContent("삭제된 댓글입니다.");
            comment.setStatus(Comment.Status.valueOf("disable")); // 상태를 'disable'로 변경
            // 필요하다면 user_id도 null로 처리하여 익명화 할 수 있습니다.
            // comment.setUserId(null);
        } else {
            // 3. 자식 댓글이 없으면, DB에서 완전히 삭제 (Hard Delete)
            //    만약 이 댓글이 다른 댓글의 자식이었다면, 부모의 children 리스트에서 자동으로 제거됩니다. (orphanRemoval=true 덕분)
            commentRepository.delete(comment);
        }
    }

    /**
     * 게시글 좋아요 추가
     */
    @Transactional
    public void addLike(Long boardId, Long userId) {
        // 1. 게시글 존재 확인
        Post post = postRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 2. 이미 좋아요를 눌렀는지 확인 (수정된 메서드 호출)
        Optional<PostLike> existingLike = postLikeRepository.findById_PostIdAndId_UserId(boardId, userId);
        if (existingLike.isPresent()) {
            throw new IllegalStateException("이미 좋아요를 누른 게시글입니다.");
        }

        // 3. PostLikeId 생성
        PostLikeId likeId = new PostLikeId();
        likeId.setPostId(boardId);
        likeId.setUserId(userId);

        // 4. PostLike 엔티티 생성 및 저장
        PostLike newLike = new PostLike();
        newLike.setId(likeId);
        newLike.setPost(post);
        postLikeRepository.save(newLike);
    }

    /**
     * 게시글 좋아요 취소
     */
    @Transactional
    public void removeLike(Long boardId, Long userId) {
        // 1. 좋아요 존재 확인 (수정된 메서드 호출)
        Optional<PostLike> existingLike = postLikeRepository.findById_PostIdAndId_UserId(boardId, userId);
        if (existingLike.isEmpty()) {
            throw new IllegalArgumentException("좋아요를 누르지 않은 게시글입니다.");
        }

        // 2. 좋아요 삭제 (수정된 메서드 호출)
        postLikeRepository.deleteById_PostIdAndId_UserId(boardId, userId);
    }

    /**
     * HOT 게시글 조회 (최근 7일간 조회수 TOP 10)
     */
    /**
     * HOT 게시글 조회 (최근 7일간 조회수 TOP 10)
     */
    @Transactional(readOnly = true) // 이제 이 어노테이션이 정상 동작합니다.
    public List<AllBoardRespDTO> getHotBoards() {
        // 1. 기준 날짜 설정 (7일 전)
        Timestamp sevenDaysAgo = Timestamp.valueOf(LocalDateTime.now().minusDays(7));

        // 2. Repository를 통해 HOT 게시글 엔티티 리스트를 조회 (수정된 메서드 호출)
        List<Post> hotPostList = postRepository.findTop10ByCreatedAtAfterOrderByViewCountDesc(sevenDaysAgo);

        // 3. DTO로 변환 (AllBoardRespDTO의 fromEntity가 알아서 처리하므로 수정 불필요)
        List<AllBoardRespDTO> dtoList = hotPostList.stream()
                .map(AllBoardRespDTO::fromEntity)
                .collect(Collectors.toList());

        // 4. 변환된 DTO 리스트 반환
        return dtoList;
    }

    /**
     * 게시글 검색 (제목 + 내용)
     */
    @Transactional(readOnly = true)
    public List<AllBoardRespDTO> searchBoards(String keyword) {
        // 1. Repository를 통해 키워드로 게시글 엔티티 리스트를 검색
        // 제목과 내용 양쪽에서 모두 동일한 키워드로 검색
        List<Post> searchResultList = postRepository.findByTitleContainingOrContentContaining(keyword, keyword);

        // 2. 검색된 엔티티 리스트를 DTO 리스트로 변환
        List<AllBoardRespDTO> dtoList = searchResultList.stream()
                .map(AllBoardRespDTO::fromEntity)
                .collect(Collectors.toList());

        // 3. 변환된 DTO 리스트 반환
        return dtoList;
    }

    /**
     * 총 사용자 수와 총 게시글 수 조회
     */
    @Transactional(readOnly = true)
    public TotalStatsDTO getTotalStats() {
        // 1. 총 게시글 수 조회
        long totalPosts = postRepository.count();

        // 2. 글을 작성한 총 사용자 수 조회
        long totalUsers = postRepository.countDistinctUsers();

        // 3. DTO에 담아 반환
        return new TotalStatsDTO(totalUsers, totalPosts);
    }
}
