package com.ottrade.ottrade.domain.community.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ottrade.ottrade.domain.community.dto.AllBoardRespDTO;
import com.ottrade.ottrade.domain.community.dto.BoardDetailRespDTO;
import com.ottrade.ottrade.domain.community.dto.BoardUpdateReqDTO;
import com.ottrade.ottrade.domain.community.dto.BoardUpdateRespDTO;
import com.ottrade.ottrade.domain.community.dto.BoardWriteDTO;
import com.ottrade.ottrade.domain.community.dto.CommentCreateRequest;
import com.ottrade.ottrade.domain.community.dto.CommentDTO;
import com.ottrade.ottrade.domain.community.dto.TotalStatsDTO;
import com.ottrade.ottrade.domain.community.entity.Post;
import com.ottrade.ottrade.domain.community.service.BoardService;
import com.ottrade.ottrade.global.util.ApiResponse;
import com.ottrade.ottrade.security.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@Tag(name = "Board", description = "커뮤니티 게시판 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다. 인증된 사용자만 가능합니다.")
    @PostMapping("/write")
    public ResponseEntity<ApiResponse<String>> boardWrite(@RequestBody BoardWriteDTO boardWriteDTO,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        String message = boardService.boardWrite(boardWriteDTO, userDetails);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(message, null));
    }

    @Operation(summary = "게시글 목록 조회", description = "타입별(예: 'free', 'info') 게시글 전체 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AllBoardRespDTO>>> getBoard(
            @Parameter(description = "게시판 타입 (e.g., free, info)", required = true) @RequestParam("type") String type) {
        List<AllBoardRespDTO> boards = boardService.allBoard(type);
        return ResponseEntity.ok(ApiResponse.success("게시글 목록 조회 성공", boards));
    }

    @Operation(summary = "게시글 수정", description = "기존 게시글을 수정합니다. 게시글 작성자 본인만 수정 가능합니다.")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<BoardUpdateRespDTO>> updateBoard(
            @RequestBody BoardUpdateReqDTO boardUpdateReqDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Post updatedPost = boardService.updateBoard(boardUpdateReqDTO, userDetails.getUser().getId());
        BoardUpdateRespDTO responseDTO = BoardUpdateRespDTO.fromEntity(updatedPost);
        return ResponseEntity.ok(ApiResponse.success("게시글이 성공적으로 수정되었습니다.", responseDTO));
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다. 게시글 작성자 본인만 삭제 가능합니다.")
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @Parameter(description = "삭제할 게시글 ID") @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        boardService.deleteBoard(boardId, userDetails.getUser().getId());
        return ResponseEntity.ok(ApiResponse.success("삭제완료"));
    }

    @Operation(summary = "게시글 상세 조회", description = "특정 게시글의 상세 내용을 조회합니다. 조회 시 조회수가 1 증가합니다.")
    @GetMapping("/detail/{boardId}")
    public ResponseEntity<ApiResponse<BoardDetailRespDTO>> getBoardDetail(
            @Parameter(description = "조회할 게시글 ID") @PathVariable Long boardId) {
        BoardDetailRespDTO boardDetail = boardService.detailBoard(boardId);
        return ResponseEntity.ok(ApiResponse.success("게시글 상세 조회 성공", boardDetail));
    }

    @Operation(summary = "댓글 작성", description = "특정 게시글에 댓글 또는 대댓글을 작성합니다.")
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<CommentDTO> createComment(
            @Parameter(description = "게시글 ID") @PathVariable Long boardId,
            @RequestBody CommentCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CommentDTO created = boardService.createComment(boardId, request, userDetails.getUser().getId());
        return ResponseEntity
                .created(URI.create("/api/boards/" + boardId + "/comments/" + created.getCommentId()))
                .body(created);
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다. 댓글 작성자 본인만 삭제 가능합니다.")
    @DeleteMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @Parameter(description = "게시글 ID") @PathVariable Long boardId,
            @Parameter(description = "삭제할 댓글 ID") @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        boardService.deleteComment(commentId, userDetails.getUser().getId());
        return ResponseEntity.ok(ApiResponse.success("댓글이 삭제되었습니다."));
    }

    @Operation(summary = "게시글 좋아요 추가", description = "특정 게시글에 '좋아요'를 추가합니다.")
    @PostMapping("/{boardId}/like")
    public ResponseEntity<ApiResponse<Void>> addLike(
            @Parameter(description = "게시글 ID") @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        boardService.addLike(boardId, userDetails.getUser().getId());
        return ResponseEntity.ok(ApiResponse.success("좋아요가 추가되었습니다."));
    }

    @Operation(summary = "게시글 좋아요 취소", description = "특정 게시글의 '좋아요'를 취소합니다.")
    @DeleteMapping("/{boardId}/like")
    public ResponseEntity<ApiResponse<Void>> removeLike(
            @Parameter(description = "게시글 ID") @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        boardService.removeLike(boardId, userDetails.getUser().getId());
        return ResponseEntity.ok(ApiResponse.success("좋아요가 취소되었습니다."));
    }

    @Operation(summary = "HOT 게시글 조회", description = "최근 7일간 조회수가 가장 높은 Top 10 게시글 목록을 조회합니다.")
    @GetMapping("/hot")
    public ResponseEntity<ApiResponse<List<AllBoardRespDTO>>> getHotBoards() {
        List<AllBoardRespDTO> hotBoards = boardService.getHotBoards();
        return ResponseEntity.ok(ApiResponse.success("HOT 게시글 조회 성공", hotBoards));
    }

    @Operation(summary = "게시글 검색", description = "제목 또는 내용에 특정 키워드가 포함된 게시글 목록을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AllBoardRespDTO>>> searchBoards(
            @Parameter(description = "검색할 키워드") @RequestParam("keyword") String keyword) {
        List<AllBoardRespDTO> searchResults = boardService.searchBoards(keyword);
        return ResponseEntity.ok(ApiResponse.success("게시글 검색 성공", searchResults));
    }

    @Operation(summary = "커뮤니티 통계 조회", description = "전체 게시글을 작성한 사용자 수와 총 게시글 수를 조회합니다.")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<TotalStatsDTO>> getTotalStats() {
        TotalStatsDTO stats = boardService.getTotalStats();
        return ResponseEntity.ok(ApiResponse.success("통계 조회 성공", stats));
    }
}