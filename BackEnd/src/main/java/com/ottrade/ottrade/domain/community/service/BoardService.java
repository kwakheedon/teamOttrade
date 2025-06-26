package com.ottrade.ottrade.domain.community.service;

import com.ottrade.ottrade.domain.community.dto.BoardWriteDTO;
import com.ottrade.ottrade.domain.community.entity.Board;
import com.ottrade.ottrade.domain.community.repository.Repository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final Repository repository;

    @Transactional
    public String boardWrite(BoardWriteDTO boardWriteDTO) {
        Board board = new Board();
        board.setUser_id(boardWriteDTO.getUser_id());
        board.setTitle(boardWriteDTO.getTitle());
        board.setContent(boardWriteDTO.getContent());
        board.setType(boardWriteDTO.getType());
        board.setStatus("enable");
        try {
            repository.save(board);
            return "성공";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
