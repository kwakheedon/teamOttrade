package com.ottrade.ottrade.exception.handler;

import com.ottrade.ottrade.util.api.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.ottrade.ottrade.exception.message.ExceptionMessage.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<?> handleBadRequest(IllegalAccessException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(ApiResponse.fail(BAD_REQUEST_MESSAGE, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleNotFound(EntityNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(ApiResponse.fail(NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleForbidden(AccessDeniedException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(ApiResponse.fail(FORBIDDEN_MESSAGE, HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
    }
}
