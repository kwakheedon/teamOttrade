package com.ottrade.ottrade.util.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data, HttpStatus status) {
        return new ApiResponse<>(status.value(), "succes", data);
    }

    public static <T> ApiResponse<T> fail(String message, HttpStatus status) {
        return new ApiResponse<>(status.value(), message, null);
    }
}
