package com.ottrade.ottrade.domain.community.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailRespDTO {

    private Long boardId;
    private String title;
    private String content;
    private Long user_id;
    private List<CommentDTO> comment;
    private int postLikeCount;


}
