package com.ottrade.ottrade.domain.community.controller;

import com.ottrade.ottrade.domain.community.dto.*;
import com.ottrade.ottrade.domain.community.entity.Board;
import com.ottrade.ottrade.domain.community.service.BoardService;
import com.ottrade.ottrade.util.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class Controller {

    private final BoardService boardService;

    // 게시글 작성
    @PostMapping("/write")
    public ResponseEntity<?> boardWrite(@RequestBody BoardWriteDTO boardWriteDTO) {
        return new ResponseEntity<>(ApiResponse.success(boardService.boardWrite(boardWriteDTO), HttpStatus.CREATED), HttpStatus.CREATED);
    }

    // 게시글 전체 목록 조회..type(free,info게시판 등등)
    @GetMapping("/")
    public ResponseEntity<?> getBoard(@RequestParam("type") String type) {
        return new ResponseEntity<>(ApiResponse.success(boardService.allBoard(type), HttpStatus.OK), HttpStatus.OK);
    }

    //게시글 수정
    @PutMapping("/update")
    public ResponseEntity<?> updateBoard(@RequestBody BoardUpdateReqDTO boardUpdateReqDTO) {
        // 서비스는 엔티티를 반환
        Board updatedBoard = boardService.updateBoard(boardUpdateReqDTO);
        // 컨트롤러에서 엔티티를 응답 DTO로 변환
        BoardUpdateRespDTO responseDTO = BoardUpdateRespDTO.fromEntity(updatedBoard);

        // 응답 DTO를 반환
        return new ResponseEntity<>(ApiResponse.success(responseDTO, HttpStatus.OK), HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return new ResponseEntity<>(ApiResponse.success("삭제완", HttpStatus.OK), HttpStatus.OK);
    }

    // 게시글 상세 조회
    @GetMapping("/detail/{boardId}")
    public ResponseEntity<?> getBoardDetail(@PathVariable Long boardId) {
        BoardDetailRespDTO boardDetailRespDTO = boardService.detailBoard(boardId);
        return new ResponseEntity<>(ApiResponse.success(boardDetailRespDTO, HttpStatus.OK), HttpStatus.OK);
    }

    /**
     * 댓글 작성
     * - 회원 인증 필요
     * - Authorization 헤더 Bearer 토큰 필요
     */
    @PostMapping("/{boardId}/{userId}/comments")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long boardId, @PathVariable Long userId,
//            @RequestHeader("Authorization") String token,
            @RequestBody CommentCreateRequest request
    ) {
        CommentDTO created = boardService.createComment(boardId, request, userId);
        return ResponseEntity
                .created(URI.create("/api/boards/" + boardId + "/comments/" + created.getCommentId()))
                .body(created);
    }
}
