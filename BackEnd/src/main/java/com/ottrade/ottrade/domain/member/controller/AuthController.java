package com.ottrade.ottrade.domain.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
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
import com.ottrade.ottrade.service.SmsService; // SmsService 임포트
import java.util.Random;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.concurrent.TimeUnit;

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
    private final SmsService smsService; // SmsService 주입
    private final StringRedisTemplate redisTemplate; // Redis 사용을 위해 주입


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

    @PostMapping("/sms")
    public ResponseEntity<ApiResponse<Void>> sendSms(@RequestBody Map<String, String> payload) {
        String to = payload.get("to");

        // 6자리 랜덤 인증번호 생성
        Random random = new Random();
        String verificationCode = String.format("%06d", random.nextInt(999999));

        // SMS 발송
        smsService.sendOne(to, verificationCode);

        // Redis에 인증번호 저장 (유효시간 3분)
        redisTemplate.opsForValue().set("sms:" + to, verificationCode, 3, TimeUnit.MINUTES);

        return ResponseEntity.ok(ApiResponse.success("인증번호가 발송되었습니다."));
    }

    @PostMapping("/sms/verify")
    public ResponseEntity<ApiResponse<Void>> verifySms(@RequestBody Map<String, String> payload) {
        String phoneNumber = payload.get("phoneNumber");
        String verificationCode = payload.get("verificationCode");

        String storedCode = redisTemplate.opsForValue().get("sms:" + phoneNumber);

        if (storedCode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("인증번호 유효시간이 초과되었습니다."));
        }

        if (storedCode.equals(verificationCode)) {
            redisTemplate.delete("sms:" + phoneNumber); // 인증 성공 시 Redis에서 삭제
            return ResponseEntity.ok(ApiResponse.success("인증에 성공했습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("인증번호가 일치하지 않습니다."));
        }
    }
    
}