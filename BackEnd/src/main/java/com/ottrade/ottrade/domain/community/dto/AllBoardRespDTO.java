package com.ottrade.ottrade.domain.community.dto;

import com.ottrade.ottrade.domain.community.entity.Board;
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
    private Long user_id;
    private String title;
    private String content;
    private String type;
    private int view_count;
    private Timestamp created_at;
    private String status;

    public static AllBoardRespDTO fromEntity(Board board) {
        AllBoardRespDTO dto = new AllBoardRespDTO();
        dto.id = board.getId();
        dto.title = board.getTitle();
        dto.content = board.getContent();
        dto.type = board.getType();
        dto.view_count = board.getViewCount();
        dto.created_at = board.getCreatedAt();
        dto.status = board.getStatus();
        return dto;
    }

}
