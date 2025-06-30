package com.ottrade.ottrade.domain.community.dto;

import com.ottrade.ottrade.domain.community.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllBoardRespDTO {

    private Long id;
    private Long user_id; // user_id 필드
    private String title;
    private String content;
    private String type;
    private int view_count;
    private Timestamp created_at;
    private String status;

    public static AllBoardRespDTO fromEntity(Post post) {
        AllBoardRespDTO dto = new AllBoardRespDTO();
        dto.id = post.getId();
        dto.user_id = post.getUserId(); // user_id 필드 매핑 추가
        dto.title = post.getTitle();
        dto.content = post.getContent();
        dto.type = post.getType();
        dto.view_count = post.getViewCount();
        dto.created_at = post.getCreatedAt();
        dto.status = post.getStatus();
        return dto;
    }
}
