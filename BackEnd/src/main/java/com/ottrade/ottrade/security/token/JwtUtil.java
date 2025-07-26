package com.ottrade.ottrade.security.token;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ottrade.ottrade.domain.member.entity.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {
	// JWT 토큰을 생성하고, 파싱하고, 검증하는 유틸 클래스
	

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
  
    
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    
    //AccessToken 토큰생성 
    public String generateAccessToken(Long userId, Role role) {
    	
    	
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put("role", role.getKey()); 

       
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationMs);

     
        return Jwts.builder()
                .setClaims(claims) //정보(Claims) 설정
                .setIssuedAt(now)  //토큰 발급 시간
                .setExpiration(expiryDate) //토큰 만료 시간
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) //서명
                .compact();
    }
    

    //RefreshToken 토큰생성
    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMs);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    //토큰유효기간 검증 (서비스)(필터)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // 토큰사용자 id추출(필터)
    public Long getUserIdFromToken(String token) {
        return Long.parseLong(getClaimsFromToken(token).getSubject());
    }
    
    
    //JWT 토큰을 헤더에서 꺼내는 코드 (필터)
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거
        }
        return null;
    }

     //토큰 Claims (조각데이터) 반환
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    
    // Authorization 헤더에서 순수 토큰만 추출하는 헬퍼 메서드 다른 데서 헤더를 문자열로 받았을 때 사용
    public String extractToken(String accessTokenHeader) {
        if (accessTokenHeader != null && accessTokenHeader.startsWith("Bearer ")) {
            return accessTokenHeader.substring(7); // "Bearer " 제거 후 순수 토큰 반환
        }
        return null;
    }
    
    
 
    
    
}