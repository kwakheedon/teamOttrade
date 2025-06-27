package com.ottrade.ottrade.domain.community.service;

import com.ottrade.ottrade.domain.community.dto.AllBoardRespDTO;
import com.ottrade.ottrade.domain.community.dto.BoardWriteDTO;
import com.ottrade.ottrade.domain.community.entity.Board;
import com.ottrade.ottrade.domain.community.repository.Repository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<AllBoardRespDTO> allBoard(String type) {
        // 1. Repository를 통해 Board 엔티티 리스트를 조회합니다.
        List<Board> boardList = repository.findByType(type);

        // 2. Stream API를 사용하여 각 Board 엔티티를 AllBoardRespDTO로 변환합니다.
        //    (fromEntity가 static 메소드라고 가정)
        List<AllBoardRespDTO> dtoList = boardList.stream()
                .map(board -> AllBoardRespDTO.fromEntity(board)) // 각 board를 DTO로 매핑
                .collect(Collectors.toList()); // 결과를 List로 수집

        // 3. 변환된 DTO 리스트를 반환합니다.
        return dtoList;
    }
}
