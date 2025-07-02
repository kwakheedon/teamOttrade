package com.ottrade.ottrade.domain.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ottrade.ottrade.domain.member.dto.AuthDto;
import com.ottrade.ottrade.domain.member.dto.AuthDto.LogoutRequest;
import com.ottrade.ottrade.domain.member.service.AuthService;
import com.ottrade.ottrade.global.exception.CustomException;
import com.ottrade.ottrade.global.exception.ErrorCode;
import com.ottrade.ottrade.global.util.ApiResponse;
import com.ottrade.ottrade.security.token.JwtUtil;
import com.ottrade.ottrade.security.user.CustomUserDetails;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;


    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody AuthDto.SignUpRequest request) {
        authService.signup(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입 성공", null));
    }



    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<AuthDto.TokenResponse>> reissue(@RequestBody AuthDto.ReissueRequest request) {
        AuthDto.TokenResponse response = authService.reissueToken(request);
        return ResponseEntity.ok(ApiResponse.success("토큰 재발급 성공", response));
    }
    
    
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginRequest request, HttpServletResponse response) {
        AuthDto.TokenResponse tokenResponse = authService.login(request);

        // 리프레시 토큰을 쿠키로 저장
        Cookie refreshCookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        response.addCookie(refreshCookie);

        // accessToken + refreshToken 모두 응답 바디로도 반환
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", tokenResponse.getAccessToken());
        tokenMap.put("refreshToken", tokenResponse.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success("로그인 성공", tokenMap));
    }
    
    
   
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest logoutRequest, HttpServletRequest request) {
        String accessToken = jwtUtil.resolveToken(request);
        String refreshToken = logoutRequest.getRefreshToken();

        if (accessToken == null || refreshToken == null) {
            throw new CustomException(ErrorCode.TOKEN_NOT_FOUND, "로그아웃에 필요한 토큰이 없습니다.");
        }

        authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok("성공적으로 로그아웃되었습니다.");
    }
    
    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdraw(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
       //String accessToken = jwtUtil.extractToken(accessTokenHeader); // "Bearer " 제거
        authService.withdraw(userDetails, userDetails.getUser());
        return ResponseEntity.ok("회원탈퇴 완료");
    }
    
}