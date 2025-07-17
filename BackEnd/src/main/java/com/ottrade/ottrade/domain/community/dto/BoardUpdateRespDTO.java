package com.ottrade.ottrade.domain.community.dto;

import com.ottrade.ottrade.domain.community.entity.Post;

import lombok.Getter;

@Getter
public class BoardUpdateRespDTO {

    private final Long id;
    private final String title;
    private final String content;

    public static BoardUpdateRespDTO fromEntity(Post post) {
        return new BoardUpdateRespDTO(post.getId(), post.getTitle(), post.getContent());
    }

    private BoardUpdateRespDTO(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
