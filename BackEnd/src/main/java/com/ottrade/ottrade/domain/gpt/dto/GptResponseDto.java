package com.ottrade.ottrade.domain.gpt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class GptResponseDto {
    private List<Choice> choices;

    @Data
    @NoArgsConstructor
    public static class Choice {
        private Message message;
    }

    @Data
    @NoArgsConstructor
    public static class Message {
        private String content;
    }
}