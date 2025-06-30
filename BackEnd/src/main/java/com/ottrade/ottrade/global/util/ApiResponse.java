package com.ottrade.ottrade.global.util;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // data가 null일 경우 json에서 제외
public class ApiResponse<T> {

    private final int status;
    private final boolean success;
    private final String message;
    private final T data;

    private ApiResponse(boolean success, String message, T data, HttpStatus status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = status.value();
    }

    // 성공 응답 (데이터 포함)
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, HttpStatus.OK);
    }

    // 성공 응답 (데이터 없음)
    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message, null, HttpStatus.OK);
    }

    // 생성 성공 응답 (CREATED 201)
    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(true, message, data, HttpStatus.CREATED);
    }

    // 실패 응답
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, HttpStatus.BAD_REQUEST);
    }

    // 실패 응답 (상태 코드 지정)
    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return new ApiResponse<>(false, message, null, status);
    }
}
