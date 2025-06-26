package com.ottrade.ottrade.domain.community.dto;

import com.ottrade.ottrade.domain.community.entity.Comment;
import com.ottrade.ottrade.domain.community.entity.PostLike;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

public class AllBoardRespDTO {

    private Long id;
    private Long user_id;
    private String title;
    private String content;
    private String type;
    private int view_count;
    private Timestamp created_at;
    private String status;

}
