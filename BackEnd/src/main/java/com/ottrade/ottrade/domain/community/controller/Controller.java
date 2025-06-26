package com.ottrade.ottrade.domain.community.controller;

import com.ottrade.ottrade.domain.community.dto.BoardWriteDTO;
import com.ottrade.ottrade.domain.community.service.BoardService;
import com.ottrade.ottrade.util.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class Controller {

    private final BoardService boardService;

    @PostMapping("/write")
    public ResponseEntity<?> boardWrite(@RequestBody BoardWriteDTO boardWriteDTO) {
        return new ResponseEntity<>(ApiResponse.success(boardService.boardWrite(boardWriteDTO), HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<?> getBoard(@RequestParam String type) {
        return new ResponseEntity<>(ApiResponse.success(boardService.allBoard(type), HttpStatus.OK), HttpStatus.OK);
    }
}
