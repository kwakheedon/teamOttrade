package com.ottrade.ottrade.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
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
    private boolean isLiked;
    public BoardDetailRespDTO(Long boardId, String title, String content, Long user_id, String nickname, LocalDateTime createdAt, List<CommentDTO> comment, int postLikeCount, boolean isLiked) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.user_id = user_id;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.comment = comment;
        this.postLikeCount = postLikeCount;
        this.isLiked = isLiked;
    }
}