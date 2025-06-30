package com.ottrade.ottrade.domain.community.dto;

import lombok.Data;

@Data
public class CommentCreateRequest {
    private String content;
    private Long parentId; // 부모 댓글 ID 필드 추가

}
