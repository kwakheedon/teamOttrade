package com.ottrade.ottrade.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor // 이 어노테이션이 모든 필드를 받는 생성자를 자동으로 만들어줍니다.
@NoArgsConstructor
public class BoardDetailRespDTO {

    private Long boardId;
    private String title;
    private String content;
    private Long user_id;
    private String nickname;
    private LocalDateTime createdAt;
    private List<CommentDTO> comment;
    private int postLikeCount;

    // "Constructor with '8' parameters is already defined" 오류를 해결하기 위해
    // 직접 추가했던 생성자를 삭제합니다.
    /*
    public BoardDetailRespDTO(Long boardId, String title, String content, Long user_id, String nickname, LocalDateTime createdAt, List<CommentDTO> comment, int postLikeCount) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.user_id = user_id;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.comment = comment;
        this.postLikeCount = postLikeCount;
    }
    */
}