package com.ottrade.ottrade.domain.member.service;

import com.ottrade.ottrade.domain.member.dto.AuthDto;
import com.ottrade.ottrade.domain.member.entity.User;
import com.ottrade.ottrade.domain.member.repository.UserRepository;
import com.ottrade.ottrade.global.exception.CustomException;
import com.ottrade.ottrade.global.exception.ErrorCode;
import com.ottrade.ottrade.security.token.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(AuthDto.SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS, "이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS, "이미 사용 중인 휴대폰 번호입니다.");
        }

        User user = request.toEntity(passwordEncoder);
        userRepository.save(user);
    }

    @Transactional  //일반로그인
    public AuthDto.TokenResponse login(AuthDto.LoginRequest request) {
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAIL, "가입되지 않은 아이디 입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAIL, "비밀번호가 일치하지 않습니다.");
        }
        return issueTokens(user);
    }
    
    
    @Transactional
    public AuthDto.TokenResponse reissueToken(AuthDto.ReissueRequest request) {
        String refreshToken = request.getRefreshToken();
        
        // 1. 리프레시 토큰 유효성 검증
        if(!jwtUtil.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN, "유효하지 않은 리프레시 토큰입니다.");
        }

        // 2. DB의 토큰과 일치하는지 확인
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NOT_FOUND, "리프레시 토큰을 찾을 수 없습니다. 다시 로그인해주세요."));

        // 3. 새로운 토큰 발급
        return issueTokens(user);
    }

    
    // 로그인 및 소셜 로그인 성공 시 토큰 발급
    @Transactional
    public AuthDto.TokenResponse issueTokens(User user) {
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        user.updateRefreshToken(refreshToken); // DB에 리프레시 토큰 저장

        return AuthDto.TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
