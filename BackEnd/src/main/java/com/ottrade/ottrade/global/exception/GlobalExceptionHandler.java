package com.ottrade.ottrade.global.exception;

import com.ottrade.ottrade.global.util.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getMessage() + " - " + ex.getDetail()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(ApiResponse.error(errorMessage));
    }
    
    @ExceptionHandler(Exception.class) // <--- 모든 종류의 예외를 잡습니다.
    public ResponseEntity<ApiResponse<Void>> handleAllExceptions(Exception ex) {
        // 서버 콘솔에 에러 상세 정보 출력 (디버깅용)
        System.err.println("예상치 못한 서버 에러 발생: " + ex.getClass().getName() + " - " + ex.getMessage());
        ex.printStackTrace(); // 스택 트레이스도 출력

        
        // 클라이언트에게는 일반적인 에러 메시지 반환
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // HTTP 500 Internal Server Error
                .body(ApiResponse.error("서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }
}
