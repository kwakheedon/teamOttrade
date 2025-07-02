package com.ottrade.ottrade.domain.member.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ottrade.ottrade.domain.community.repository.CommentRepository;
import com.ottrade.ottrade.domain.community.repository.PostLikeRepository;
import com.ottrade.ottrade.domain.community.repository.PostRepository;
import com.ottrade.ottrade.domain.log.repository.SearchLogRepository;
import com.ottrade.ottrade.domain.member.dto.AuthDto;
import com.ottrade.ottrade.domain.member.entity.User;
import com.ottrade.ottrade.domain.member.repository.RefreshRepository;
import com.ottrade.ottrade.domain.member.repository.UserRepository;
import com.ottrade.ottrade.global.exception.CustomException;
import com.ottrade.ottrade.global.exception.ErrorCode;
import com.ottrade.ottrade.security.token.JwtUtil;
import com.ottrade.ottrade.security.user.CustomUserDetails;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import java.util.concurrent.TimeUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate redisTemplate; 
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final RefreshRepository refreshRepository;
    private final SearchLogRepository searchLogRepository;
	
	

	
	// 회원가입
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

	
	
	// 일반로그인
	@Transactional
	public AuthDto.TokenResponse login(AuthDto.LoginRequest request) {
		User user = userRepository.findByPhone(request.getPhone())
				.orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAIL, "가입되지 않은 아이디 입니다."));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new CustomException(ErrorCode.LOGIN_FAIL, "비밀번호가 일치하지 않습니다.");
		}
		return issueTokens(user);
	}

	
	
	//토근 재발급
	@Transactional
	public AuthDto.TokenResponse reissueToken(AuthDto.ReissueRequest request) {
		String refreshToken = request.getRefreshToken();

		// 1. 리프레시 토큰 유효성 검증
		if (!jwtUtil.validateToken(refreshToken)) {
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

		return AuthDto.TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
	}

	
	
	// 로그아웃
	@Transactional 
	public void logout(String accessToken, String refreshToken) {
	   //로그아웃 순서를 명시해주는 -> 메서드
		validateAccessTokenOrThrow(accessToken);
		deleteRefreshTokenFromDB(refreshToken);
//		blacklistAccessToken(accessToken);
	}


	private void validateAccessTokenOrThrow(String accessToken) {
		if (!jwtUtil.validateToken(accessToken)) {
			throw new CustomException(ErrorCode.INVALID_TOKEN, "로그아웃 요청: 유효하지 않은 토큰입니다.");
		}
	}

	//// 서버돌릴때 도커로 실행 
//	private void blacklistAccessToken(String accessToken) {
//		// JwtUtil의 getClaimsFromToken을 사용하여 만료 시간을 가져옵니다.
//		Claims claims = jwtUtil.getClaimsFromToken(accessToken);
//		Date expiration = claims.getExpiration();
//		long remainingTime = expiration.getTime() - System.currentTimeMillis();
//
//		// 토큰의 유효시간이 남아있는 경우에만 블랙리스트에 추가합니다.
//		if (remainingTime > 0) {
//			redisTemplate.opsForValue().set(accessToken, "logout", remainingTime, TimeUnit.MILLISECONDS);
//		}
//	}

	private void deleteRefreshTokenFromDB(String refreshToken) {
		userRepository.findByRefreshToken(refreshToken).ifPresent(user -> {
			user.setRefreshToken(null);
		});
	}
	

		//회원탈퇴
	    @Transactional
	    public void withdraw(CustomUserDetails userDetails, User user) {
	     
	        Long userId = user.getId();

	        // DB에 저장된 리프레시 토큰을 삭제하여 토큰 재발급을 막습니다.
	        refreshRepository.deleteById(userId);

	        // 사용자가 작성한 모든 연관 데이터를 삭제하여 외래 키 제약 조건 위반을 방지합니다.
	        searchLogRepository.deleteAllByUserId(userId);
	        postLikeRepository.deleteAllByUserId(userId);
	        commentRepository.deleteAllByUserId(userId);
	        postRepository.deleteAllByUserId(userId);

	        userRepository.delete(user);
	    }
	
	 

		
}