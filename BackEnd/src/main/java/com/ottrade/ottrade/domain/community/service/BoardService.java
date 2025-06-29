package com.ottrade.ottrade.domain.community.service;

import com.ottrade.ottrade.domain.community.dto.*;
import com.ottrade.ottrade.domain.community.entity.Board;
import com.ottrade.ottrade.domain.community.entity.Comment;
import com.ottrade.ottrade.domain.community.repository.CommentRepository;
import com.ottrade.ottrade.domain.community.repository.PostLikeRepository;
import com.ottrade.ottrade.domain.community.repository.Repository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final Repository repository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public String boardWrite(BoardWriteDTO boardWriteDTO) {
        Board board = new Board();
        board.setUser_id(boardWriteDTO.getUser_id());
        board.setTitle(boardWriteDTO.getTitle());
        board.setContent(boardWriteDTO.getContent());
        board.setType(boardWriteDTO.getType());
        board.setStatus("enable");
        try {
            repository.save(board);
            return "성공";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<AllBoardRespDTO> allBoard(String type) {
        // 1. Repository를 통해 Board 엔티티 리스트를 조회합니다.
        List<Board> boardList = repository.findByType(type);

        // 2. Stream API를 사용하여 각 Board 엔티티를 AllBoardRespDTO로 변환합니다.
        //    (fromEntity가 static 메소드라고 가정)
        List<AllBoardRespDTO> dtoList = boardList.stream()
                .map(board -> AllBoardRespDTO.fromEntity(board)) // 각 board를 DTO로 매핑
                .collect(Collectors.toList()); // 결과를 List로 수집

        // 3. 변환된 DTO 리스트를 반환합니다.
        return dtoList;
    }

    @Transactional
    public Board updateBoard(BoardUpdateReqDTO boardUpdateReqDTO) {
        Board boardUpdate = repository.findById(boardUpdateReqDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID " + boardUpdateReqDTO.getId() + "에 해당하는 게시글을 찾을 수 없습니다."));

        boardUpdate.setTitle(boardUpdateReqDTO.getTitle());
        boardUpdate.setContent(boardUpdateReqDTO.getContent());

        return boardUpdate;
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        if (!repository.existsById(boardId)) {
            throw new IllegalArgumentException("ID " + boardId + "에 해당하는 게시글을 찾을 수 없습니다.");
        }

        // 추가된 로직: 게시글에 연관된 댓글과 좋아요를 먼저 삭제합니다.
        commentRepository.deleteAllByPostId(boardId);
        postLikeRepository.deleteAllByBoardId(boardId); // PostLikeRepository에도 비슷한 메서드가 필요합니다.

        repository.deleteById(boardId);
    }

    @Transactional()
    public BoardDetailRespDTO detailBoard(Long boardId) {
        // 게시글 조회
        Board board = repository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // --- 댓글 조회 및 구조화 로직 수정 ---
        // 게시글에 달린 모든 댓글을 가져온다.
        List<Comment> comments = commentRepository.findByPostId(boardId);

        // 최상위 댓글(부모가 없는 댓글)만 필터링하여 DTO로 변환한다.
        // DTO 변환 과정에서 자식 댓글들이 재귀적으로 처리된다.
        List<CommentDTO> commentDTOs = comments.stream()
                .filter(c -> c.getParent() == null)
                .map(CommentDTO::new)
                .collect(Collectors.toList());

        long likeCount = postLikeRepository.countByBoardId(boardId);

        // DTO 매핑
        return new BoardDetailRespDTO(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getUser_id(),
                commentDTOs,
                (int) likeCount
        );
    }

    @Transactional
    public CommentDTO createComment(Long boardId, CommentCreateRequest request, Long userId) {

        Board board = repository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        comment.setContent(request.getContent());
        comment.setStatus("enable");
        comment.setPost(board);
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
}
