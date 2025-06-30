package com.ottrade.ottrade.security.config;

import com.ottrade.ottrade.security.filter.JwtAuthenticationFilter;
import com.ottrade.ottrade.security.handler.OAuth2LoginSuccessHandler;
import com.ottrade.ottrade.security.user.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. 기본 설정: CSRF, 세션, 기본 로그인 폼 비활성화
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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

     
        http
            .oauth2Login(oauth2 -> oauth2
               
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
               
                .successHandler(oAuth2LoginSuccessHandler)
            );

     
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}