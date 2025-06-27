package com.ottrade.ottrade.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long commentId;
    private Long user_id;      // 댓글 작성자 ID
    private String content;
    private Timestamp createdAt;
}