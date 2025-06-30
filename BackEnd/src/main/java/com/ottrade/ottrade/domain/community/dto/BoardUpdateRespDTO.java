package com.ottrade.ottrade.domain.community.dto;

import com.ottrade.ottrade.domain.community.entity.Post;

import lombok.Getter;

@Getter
public class BoardUpdateRespDTO {

    private final Long id;
    private final String title;
    private final String content;

    // Board 엔티티를 DTO로 변환하는 정적 팩토리 메소드
    public static BoardUpdateRespDTO fromEntity(Post post) {
        return new BoardUpdateRespDTO(post.getId(), post.getTitle(), post.getContent());
    }

    // 생성자
    private BoardUpdateRespDTO(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
