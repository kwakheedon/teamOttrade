package com.ottrade.ottrade.domain.community.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardWriteDTO {

    private Long user_id;
    private String title;
    private String content;
    private String type;

}
