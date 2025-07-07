package com.ottrade.ottrade.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173", "http://192.168.0.6:5173", "http://223.130.136.83:5173") // 프론트엔드 서버 주소 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // 쿠키 등 자격 증명 허용
                .maxAge(3600); // pre-flight 요청의 캐시 시간(초)
    }
}