package com.ottrade.ottrade.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ottrade.ottrade.security.filter.JwtAuthenticationFilter;
import com.ottrade.ottrade.security.handler.OAuth2LoginSuccessHandler;
import com.ottrade.ottrade.security.user.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    // 보안이 어떻게 처리되는지를 정의 
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. 기본 설정: CSRF, 세션, 기본 로그인 폼 비활성화
        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable);

        http
            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                        "/",
                        "/auth/**",
                        "/oauth2/**",           // OAuth2 로그인 시작 주소 (예: /oauth2/authorization/google)
                        "/login/oauth2/code/**" // OAuth2 로그인 성공 후 콜백 주소
                ).permitAll()
                // /user/me 경로는 USER 또는 ADMIN 권한이 있어야 접근 가능
                .requestMatchers("/user/me").hasAnyRole("USER", "ADMIN")

                .anyRequest().authenticated()
            );

        	// 이쪽에서 소셜로그인 주소로 이동시킴 
        http
            .oauth2Login(oauth2 -> oauth2

                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))

                .successHandler(oAuth2LoginSuccessHandler)
            );


        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}