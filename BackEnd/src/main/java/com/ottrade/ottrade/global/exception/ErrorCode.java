package com.ottrade.ottrade.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Member
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "M002", "이미 가입된 사용자입니다."),
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "M003", "이메일 또는 비밀번호가 일치하지 않습니다."),

    // Token
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "T001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "T002", "만료된 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "T003", "토큰을 찾을 수 없습니다."),

    // General (통합된 예외)
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "G001", "존재하지 않는 리소스입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "G002", "접근 권한이 없습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "G003", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G999", "서버 내부 오류가 발생했습니다. 관리자에게 문의해주세요.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}