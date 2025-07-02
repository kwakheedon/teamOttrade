package com.ottrade.ottrade.domain.member.controller;

import com.ottrade.ottrade.domain.member.dto.AuthDto;
import com.ottrade.ottrade.domain.member.service.UserService; // UserService 임포트
import com.ottrade.ottrade.global.util.ApiResponse;
import com.ottrade.ottrade.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; 

    	
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthDto.UserInfoResponse>> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 실제 로직 처리는 서비스 계층에 위임합니다.
        AuthDto.UserInfoResponse userInfo = userService.getMyInfo(userDetails);

        // 서비스로부터 받은 결과를 최종 응답 형태로 만들어 반환합니다.
        return ResponseEntity.ok(ApiResponse.success("내 정보 조회 성공", userInfo));
    }
}

