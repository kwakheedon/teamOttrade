package com.ottrade.ottrade.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // RestTemplate이 기본으로 사용하는 StringHttpMessageConverter의 인코딩을 UTF-8로 설정합니다.
        // 이렇게 하면 외부 API에서 한글 데이터를 받아올 때 깨지는 현상을 방지할 수 있습니다.
        restTemplate.getMessageConverters()
                .stream()
                .filter(converter -> converter instanceof StringHttpMessageConverter)
                .forEach(converter -> ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8));
        return restTemplate;
    }
}