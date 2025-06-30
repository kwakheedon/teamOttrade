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
    private Long user_id;      // 댓글 작성자 ID
    private String content;
    private Timestamp createdAt;
    private List<CommentDTO> children; // 대댓글 DTO 리스트 추가

    // 엔티티를 DTO로 변환하는 생성자 추가
    public CommentDTO(Comment comment) {
        this.commentId = comment.getId();
        this.user_id = comment.getUserId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        // 자식 댓글이 있으면 DTO로 변환해서 리스트에 담는다.
        if (comment.getChildren() != null) {
            this.children = comment.getChildren().stream()
                    .map(CommentDTO::new)
                    .collect(Collectors.toList());
        }
    }
}