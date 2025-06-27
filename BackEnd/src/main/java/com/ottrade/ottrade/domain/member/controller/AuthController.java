package com.ottrade.ottrade.domain.member.controller;

import com.ottrade.ottrade.domain.member.dto.AuthDto;
import com.ottrade.ottrade.domain.member.service.AuthService;
import com.ottrade.ottrade.global.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody AuthDto.SignUpRequest request) {
        authService.signup(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입 성공", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthDto.TokenResponse>> login(@RequestBody AuthDto.LoginRequest request) {
        AuthDto.TokenResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("로그인 성공", response));
    }
    
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<AuthDto.TokenResponse>> reissue(@RequestBody AuthDto.ReissueRequest request) {
        AuthDto.TokenResponse response = authService.reissueToken(request);
        return ResponseEntity.ok(ApiResponse.success("토큰 재발급 성공", response));
    }
}
