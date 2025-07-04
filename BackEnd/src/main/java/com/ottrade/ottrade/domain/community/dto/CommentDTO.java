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
    private String nickname; // 닉네임 필드
    private String content;
    private Timestamp createdAt;
    private List<CommentDTO> children;

    // 엔티티를 DTO로 변환하는 생성자 수정
    public CommentDTO(Comment comment) {
        this.commentId = comment.getId();
        this.user_id = comment.getUserId();
        // JOIN FETCH로 함께 조회된 User 객체에서 닉네임을 가져옵니다.
        this.nickname = (comment.getUser() != null) ? comment.getUser().getNickname() : "알 수 없음";
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        if (comment.getChildren() != null) {
            this.children = comment.getChildren().stream()
                    .map(CommentDTO::new) // 재귀적으로 자식 댓글도 변환
                    .collect(Collectors.toList());
        }
    }
}