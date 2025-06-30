package com.ottrade.ottrade.domain.community.controller;

import com.ottrade.ottrade.domain.community.dto.*;
import com.ottrade.ottrade.domain.community.entity.Post;
import com.ottrade.ottrade.domain.community.service.BoardService;

import com.ottrade.ottrade.global.util.ApiResponse;
import com.ottrade.ottrade.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 작성
     * @param boardWriteDTO 게시글 작성 정보
     * @return 생성 성공 응답
     */
    @PostMapping("/write")
    public ResponseEntity<ApiResponse<String>> boardWrite(@RequestBody BoardWriteDTO boardWriteDTO) {
        String message = boardService.boardWrite(boardWriteDTO);
        // 생성(201) 상태 코드와 함께 성공 메시지를 반환합니다. 데이터는 없습니다.
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(message, null));
    }

    /**
     * 게시글 전체 목록 조회 (타입별)
     * @param type 게시판 타입 (e.g., "free", "info")
     * @return 게시글 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AllBoardRespDTO>>> getBoard(@RequestParam("type") String type) {
        List<AllBoardRespDTO> boards = boardService.allBoard(type);
        // .toString()을 제거하고 List<AllBoardRespDTO> 객체를 직접 반환합니다.
        return ResponseEntity.ok(ApiResponse.success("게시글 목록 조회 성공", boards));
    }

    /**
     * 게시글 수정
     * @param boardUpdateReqDTO 수정할 게시글 정보
     * @param userDetails 현재 인증된 사용자 정보
     * @return 수정된 게시글 정보
     */
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<BoardUpdateRespDTO>> updateBoard(@RequestBody BoardUpdateReqDTO boardUpdateReqDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Post updatedPost = boardService.updateBoard(boardUpdateReqDTO, userDetails.getUser().getId());
        BoardUpdateRespDTO responseDTO = BoardUpdateRespDTO.fromEntity(updatedPost);
        return ResponseEntity.ok(ApiResponse.success("게시글이 성공적으로 수정되었습니다.", responseDTO));
    }

    /**
     * 게시글 삭제
     * @param boardId 삭제할 게시글 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        // Void 타입으로 데이터 없이 성공 메시지만 반환합니다.
        return ResponseEntity.ok(ApiResponse.success("삭제완료"));
    }

    /**
     * 게시글 상세 조회
     * @param boardId 조회할 게시글 ID
     * @return 게시글 상세 정보
     */
    @GetMapping("/detail/{boardId}")
    public ResponseEntity<ApiResponse<BoardDetailRespDTO>> getBoardDetail(@PathVariable Long boardId) {
        BoardDetailRespDTO boardDetail = boardService.detailBoard(boardId);
        // String.valueOf()를 제거하고 BoardDetailRespDTO 객체를 직접 반환합니다.
        return ResponseEntity.ok(ApiResponse.success("게시글 상세 조회 성공", boardDetail));
    }

    /**
     * 댓글 작성
     * @param boardId 게시글 ID
     * @param request 댓글 내용
     * @param userDetails 현재 인증된 사용자 정보
     * @return 생성된 댓글 정보
     */
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long boardId,
            @RequestBody CommentCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CommentDTO created = boardService.createComment(boardId, request, userDetails.getUser().getId());
        return ResponseEntity
                .created(URI.create("/api/boards/" + boardId + "/comments/" + created.getCommentId()))
                .body(created);
    }

    /**
     * 댓글 삭제
     * @param commentId 삭제할 댓글 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long boardId, @PathVariable Long commentId) {
        boardService.deleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.success("댓글이 삭제되었습니다."));
    }

    /**
     * 게시글 좋아요
     * @param boardId 게시글 ID
     * @param userDetails 현재 인증된 사용자 정보
     * @return 성공 메시지
     */
    @PostMapping("/{boardId}/like")
    public ResponseEntity<ApiResponse<Void>> addLike(@PathVariable Long boardId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        boardService.addLike(boardId, userDetails.getUser().getId());
        return ResponseEntity.ok(ApiResponse.success("좋아요가 추가되었습니다."));
    }

    /**
     * 게시글 좋아요 취소
     * @param boardId 게시글 ID
     * @param userDetails 현재 인증된 사용자 정보
     * @return 성공 메시지
     */
    @DeleteMapping("/{boardId}/like")
    public ResponseEntity<ApiResponse<Void>> removeLike(@PathVariable Long boardId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        boardService.removeLike(boardId, userDetails.getUser().getId());
        return ResponseEntity.ok(ApiResponse.success("좋아요가 취소되었습니다."));
    }

    /**
     * HOT 게시글 조회
     * @return HOT 게시글 목록
     */
    @GetMapping("/hot")
    public ResponseEntity<ApiResponse<List<AllBoardRespDTO>>> getHotBoards() {
        List<AllBoardRespDTO> hotBoards = boardService.getHotBoards();
        // .toString()을 제거하고 List<AllBoardRespDTO> 객체를 직접 반환합니다.
        return ResponseEntity.ok(ApiResponse.success("HOT 게시글 조회 성공", hotBoards));
    }

    /**
     * 게시글 검색 (제목 + 내용)
     * @param keyword 검색어
     * @return 검색 결과 목록
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AllBoardRespDTO>>> searchBoards(@RequestParam("keyword") String keyword) {
        List<AllBoardRespDTO> searchResults = boardService.searchBoards(keyword);
        // .toString()을 제거하고 List<AllBoardRespDTO> 객체를 직접 반환합니다.
        return ResponseEntity.ok(ApiResponse.success("게시글 검색 성공", searchResults));
    }

    /**
     * 총 사용자 및 게시글 수 통계 조회
     * @return 통계 정보
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<TotalStatsDTO>> getTotalStats() {
        TotalStatsDTO stats = boardService.getTotalStats();
        // String.valueOf()를 제거하고 TotalStatsDTO 객체를 직접 반환합니다.
        return ResponseEntity.ok(ApiResponse.success("통계 조회 성공", stats));
    }
}