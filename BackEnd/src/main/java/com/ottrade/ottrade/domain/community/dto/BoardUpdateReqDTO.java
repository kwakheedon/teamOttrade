package com.ottrade.ottrade.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateReqDTO {

    private Long id;
    private String title;
    private String content;

}
