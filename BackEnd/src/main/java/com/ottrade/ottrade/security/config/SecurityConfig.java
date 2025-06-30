package com.ottrade.ottrade.security.config;

import com.ottrade.ottrade.security.filter.JwtAuthenticationFilter;
import com.ottrade.ottrade.security.handler.OAuth2LoginSuccessHandler;
import com.ottrade.ottrade.security.user.CustomOAuth2UserService;

import org.springframework.http.HttpMethod; // HttpMethod import 추가
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authorizeHttpRequests(auth -> auth
                        // 인증 및 회원가입 관련 경로는 모두 허용
                        .requestMatchers("/auth/**", "/login/oauth2/**", "/oauth2/**").permitAll()
                        // 게시판 관련 GET 요청은 모두 허용 (비로그인 사용자도 조회 가능)
                        .requestMatchers(HttpMethod.GET, "/board/**").permitAll()
                        // HS코드 검색 관련 GET 요청 허용
                        .requestMatchers(HttpMethod.GET, "/search-summary/**", "/grouped/**", "/top3/**").permitAll()
                        // 내 정보 조회는 'USER', 'ADMIN' 역할 필요 (수정됨)
                        .requestMatchers("/api/users/me").hasAnyRole("USER", "ADMIN")
                        // 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                );


        http
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            // 인증되지 않은 요청에 대해 401 Unauthorized 응답을 보냅니다.
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
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