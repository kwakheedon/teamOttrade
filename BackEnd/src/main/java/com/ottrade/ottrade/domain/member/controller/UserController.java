package com.ottrade.ottrade.domain.member.controller;

import com.ottrade.ottrade.domain.member.dto.AuthDto;
import com.ottrade.ottrade.global.util.ApiResponse;
import com.ottrade.ottrade.security.user.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthDto.UserInfoResponse>> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        AuthDto.UserInfoResponse userInfo = AuthDto.UserInfoResponse.fromEntity(userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success("내 정보 조회 성공", userInfo));
    }
}
