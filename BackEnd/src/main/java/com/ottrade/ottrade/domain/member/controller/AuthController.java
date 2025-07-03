package com.ottrade.ottrade.domain.member.controller;

import com.ottrade.ottrade.domain.member.dto.AuthDto;
import com.ottrade.ottrade.domain.member.dto.AuthDto.LogoutRequest;
import com.ottrade.ottrade.domain.member.dto.UpdateReq;
import com.ottrade.ottrade.domain.member.dto.UpdateRes;
import com.ottrade.ottrade.domain.member.service.AuthService;
import com.ottrade.ottrade.domain.member.service.UserService;
import com.ottrade.ottrade.global.exception.CustomException;
import com.ottrade.ottrade.global.exception.ErrorCode;
import com.ottrade.ottrade.global.util.ApiResponse;
import com.ottrade.ottrade.security.token.JwtUtil;
import com.ottrade.ottrade.security.user.CustomUserDetails;
import com.ottrade.ottrade.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Tag(name = "Auth", description = "인증 및 회원가입/탈퇴 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final SmsService smsService;
    private final StringRedisTemplate redisTemplate;
    private final UserService userService;
    

    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 닉네임, 전화번호를 받아 회원가입을 처리합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 사용 중인 이메일 또는 휴대폰 번호")
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody AuthDto.SignUpRequest request) {
        authService.signup(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입 성공", null));
    }


    @Operation(summary = "토큰 재발급", description = "유효한 Refresh 토큰으로 새로운 Access/Refresh 토큰을 재발급합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<AuthDto.TokenResponse>> reissue(@RequestBody AuthDto.ReissueRequest request) {
        AuthDto.TokenResponse response = authService.reissueToken(request);
        return ResponseEntity.ok(ApiResponse.success("토큰 재발급 성공", response));
    }


    @Operation(summary = "로그인", description = "전화번호와 비밀번호로 로그인하고 Access/Refresh 토큰을 발급합니다. Refresh 토큰은 쿠키로도 설정됩니다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginRequest request, HttpServletResponse response) {
        AuthDto.TokenResponse tokenResponse = authService.login(request);

        Cookie refreshCookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        response.addCookie(refreshCookie);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", tokenResponse.getAccessToken());
        tokenMap.put("refreshToken", tokenResponse.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success("로그인 성공", tokenMap));
    }


    @Operation(summary = "로그아웃", description = "사용자의 Access 토큰을 만료 처리하고 Refresh 토큰을 삭제합니다.")
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

    
    @Operation(summary = "회원 탈퇴", description = "인증된 사용자의 계정을 삭제하고 관련 데이터를 모두 제거합니다.")
    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdraw(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        authService.withdraw(userDetails, userDetails.getUser());
        return ResponseEntity.ok("회원탈퇴 완료");
    }

    
    @Operation(summary = "SMS 인증번호 발송", description = "사용자의 휴대폰 번호로 6자리 랜덤 인증번호를 발송합니다.")
    @PostMapping("/sms")
    public ResponseEntity<ApiResponse<Void>> sendSms(@RequestBody @Parameter(description = "수신자 핸드폰 번호", required = true, schema = @Schema(type = "object", example = "{\"to\": \"01012345678\"}")) Map<String, String> payload) {
        String to = payload.get("to");

        Random random = new Random();
        String verificationCode = String.format("%06d", random.nextInt(999999));

        smsService.sendOne(to, verificationCode);

        redisTemplate.opsForValue().set("sms:" + to, verificationCode, 3, TimeUnit.MINUTES);

        return ResponseEntity.ok(ApiResponse.success("인증번호가 발송되었습니다."));
    }

    
    
    @Operation(summary = "SMS 인증번호 확인", description = "사용자가 입력한 인증번호가 유효한지 확인합니다.")
    @PostMapping("/sms/verify")
    public ResponseEntity<ApiResponse<Void>> verifySms(@RequestBody @Parameter(description = "핸드폰 번호와 인증번호", required = true, schema = @Schema(type = "object", example = "{\"phoneNumber\": \"01012345678\", \"verificationCode\": \"123456\"}")) Map<String, String> payload) {
        String phoneNumber = payload.get("phoneNumber");
        String verificationCode = payload.get("verificationCode");

        String storedCode = redisTemplate.opsForValue().get("sms:" + phoneNumber);

        if (storedCode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("인증번호 유효시간이 초과되었습니다."));
        }

        if (storedCode.equals(verificationCode)) {
            redisTemplate.delete("sms:" + phoneNumber);
            return ResponseEntity.ok(ApiResponse.success("인증에 성공했습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("인증번호가 일치하지 않습니다."));
        }
    }
    
    @Operation(summary = "내 정보 조회", description = "인증된 사용자의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/me") 
    public ResponseEntity<ApiResponse<AuthDto.UserInfoResponse>> getMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {

        AuthDto.UserInfoResponse userInfo = userService.getMyInfo(userDetails);
        return ResponseEntity.ok(ApiResponse.success("내 정보 조회 성공", userInfo));
    }

    @Operation(summary = "내 정보 수정", description = "인증된 사용자의 닉네임 또는 비밀번호를 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "정보 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "닉네임 중복 또는 비밀번호 불일치")
    })
    @PutMapping("/me") // PUT /auth/me 엔드포인트
    public ResponseEntity<ApiResponse<UpdateRes>> updateMyInfo( // 반환 타입은 UpdateRes 유지
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateReq updateRequest) { // <-- 여기가 핵심! 요청 DTO를 MemberUpdateReq로, 변수명도 updateRequest로 변경
        
        UpdateRes updatedUserInfo = userService.updateMyInfo(userDetails.getUser().getId(), updateRequest); // userService 호출 시 올바른 변수명 사용
        return ResponseEntity.ok(ApiResponse.success(updatedUserInfo.getMessage(), updatedUserInfo)); // 메시지를 DTO에서 가져옴
    }
}
    

    
    
    
    
