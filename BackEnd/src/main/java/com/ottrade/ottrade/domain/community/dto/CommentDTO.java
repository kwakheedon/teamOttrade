package com.ottrade.ottrade.domain.community.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import com.ottrade.ottrade.domain.community.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long commentId;
    private Long user_id;
    private String nickname;
    private String content;
    private Timestamp createdAt;
    private List<CommentDTO> children;

    public CommentDTO(Comment comment) {
        this.commentId = comment.getId();
        this.user_id = comment.getUserId();
        this.nickname = (comment.getUser() != null) ? comment.getUser().getNickname() : "알 수 없음";
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        if (comment.getChildren() != null) {
            this.children = comment.getChildren().stream()
                    .map(CommentDTO::new)
                    .collect(Collectors.toList());
        }
    }
}