package com.ottrade.ottrade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // UTF-8로 기본 메시지 컨버터 설정
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (int i = 0; i < converters.size(); i++) {
            HttpMessageConverter<?> converter = converters.get(i);
            if (converter instanceof StringHttpMessageConverter) {
                converters.set(i, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            }
        }

        return restTemplate;
    }
}
