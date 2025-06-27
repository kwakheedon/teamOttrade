package com.ottrade.ottrade.domain.community.dto;

import com.ottrade.ottrade.domain.community.entity.Board;
import lombok.Getter;

@Getter
public class BoardUpdateRespDTO {

    private final Long id;
    private final String title;
    private final String content;
    // 필요하다면 수정 시간 등 추가 정보를 넣을 수 있습니다.

    // Board 엔티티를 DTO로 변환하는 정적 팩토리 메소드
    public static BoardUpdateRespDTO fromEntity(Board board) {
        return new BoardUpdateRespDTO(board.getId(), board.getTitle(), board.getContent());
    }

    // 생성자
    private BoardUpdateRespDTO(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
