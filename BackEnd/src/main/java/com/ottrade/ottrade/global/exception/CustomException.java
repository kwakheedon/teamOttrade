package com.ottrade.ottrade.global.exception;

import lombok.Getter;

@Getter  //예외처리(에러코드)
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String detail; //추가 상세메세지
    private static final long serialVersionUID = 1L;
    
    // 1. ErrorCode와 상세 메시지(detail)를 모두 받는 생성자
    public CustomException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = detail;
    }

    // 2. ErrorCode만 받는 생성자 (기존의 userNotFound 생성자를 대체)
    // 이 생성자는 detail을 ErrorCode의 message로 기본 설정 
    public CustomException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage()); 
    }
    
}