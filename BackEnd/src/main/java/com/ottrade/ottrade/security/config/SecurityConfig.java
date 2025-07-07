package com.ottrade.ottrade.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // HttpMethod 임포트 추가
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ottrade.ottrade.security.filter.JwtAuthenticationFilter;
import com.ottrade.ottrade.security.user.CustomOAuth2UserService;
import com.ottrade.ottrade.security.user.OAuth2LoginSuccessHandler;

import jakarta.servlet.http.HttpServletResponse; // HttpServletResponse 임포트 추가
import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    // 외부에서 들어온 요청에 대해 보안

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. 기본 설정: CSRF, 세션, 기본 로그인 폼 비활성화
        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable);

        http
                .authorizeHttpRequests(auth -> auth
                        // 인증 및 회원가입 관련 경로는 모두 허용
                        .requestMatchers("/", "/auth/**", "/oauth2/**", "/login/oauth2/**").permitAll()
                        // 게시판 관련 GET 요청은 모두 허용 (비로그인 사용자도 조회 가능)
                        .requestMatchers(HttpMethod.GET, "/board/**").permitAll()
                        // HS코드 검색 관련 GET 요청 허용
                        .requestMatchers(HttpMethod.GET, "/search-summary/**", "/grouped/**", "/top3/**", "/share").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/logs/top-keywords").permitAll()
                        // Swagger UI 접근 허용
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // AI 분석 API 모든 사용자에게 허용
                        .requestMatchers("/gpt/**").permitAll()
                        .requestMatchers("/api/users/me", "/api/logs/**").hasAnyRole("USER", "ADMIN")
                        // 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                // 이쪽에서 소셜로그인 주소로 이동시킴
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            // 인증되지 않은 요청에 대해 401 Unauthorized 응답을 보냅니다.
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}