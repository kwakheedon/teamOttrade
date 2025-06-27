package com.ottrade.ottrade.domain.community.service;

import com.ottrade.ottrade.domain.community.dto.*;
import com.ottrade.ottrade.domain.community.entity.Board;
import com.ottrade.ottrade.domain.community.entity.Comment;
import com.ottrade.ottrade.domain.community.repository.CommentRepository;
import com.ottrade.ottrade.domain.community.repository.PostLikeRepository;
import com.ottrade.ottrade.domain.community.repository.Repository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Transactional // 1. 트랜잭션 처리를 위해 어노테이션 추가
    public void deleteBoard(Long boardId) { // 2. 반환 타입으로 void 명시

        // 3. 삭제하려는 게시글이 존재하는지 먼저 확인
        if (!repository.existsById(boardId)) {
            // 존재하지 않으면 예외 발생 (이전에 만든 커스텀 예외 재활용)
            throw new IllegalArgumentException("ID " + boardId + "에 해당하는 게시글을 찾을 수 없습니다.");
        }

        // 4. 존재하면 삭제 실행 (return 키워드 제거)
        repository.deleteById(boardId);
    }

    @Transactional()
    public BoardDetailRespDTO detailBoard(Long boardId) {
        // 게시글 조회
        Board board = repository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 댓글, 좋아요 수 조회
        List<Comment> comments = commentRepository.findByPostId(boardId);
        List<CommentDTO> commentDTOs = comments.stream()
                .map(c -> new CommentDTO(c.getId(), c.getUserId(), c.getContent(), c.getCreatedAt()))
                .toList();
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
}
