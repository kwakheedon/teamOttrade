package com.ottrade.ottrade.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ottrade.ottrade.security.token.JwtUtil;
import com.ottrade.ottrade.security.user.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    
    
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 필터를 거치지 않을 경로들을 설정 
        String[] CROSNotFilterArray = {
            "/auth/reissue",
            "/auth/signup",
            "/auth/login",
            "/oauth2/authorization/google",
            "/login/oauth2/code/google"
        };
        String path = request.getRequestURI();

        // for-loop를 사용하여 경로 일치 여부 확인
        for (String uri : CROSNotFilterArray) {
            if (path.startsWith(uri)) {
                return true;
            }
        }
        return false;
    }

     
    @Override //필터로 추출해서 SecurityContext에 저장   컨트롤러 메소드 실행 및 @AuthenticationPrincipal 활용
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.resolveToken(request);

        if (token != null && jwtUtil.validateToken(token)) {
            Long userId = jwtUtil.getUserIdFromToken(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(String.valueOf(userId));
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
