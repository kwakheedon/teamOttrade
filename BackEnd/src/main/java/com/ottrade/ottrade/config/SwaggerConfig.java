package com.ottrade.ottrade.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // API 문서의 제목, 버전 등 기본 정보를 설정합니다.
        Info info = new Info()
                .title("Ottrade API Document")
                .version("v0.0.1")
                .description("Ottrade 프로젝트의 API 명세서입니다.");

        // JWT 인증 방식을 설정합니다.
        String jwtSchemeName = "jwtAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP) // HTTP 방식을 사용
                        .scheme("bearer") // "bearer" 토큰 스킴을 사용
                        .bearerFormat("JWT")); // 토큰 형식은 JWT

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}