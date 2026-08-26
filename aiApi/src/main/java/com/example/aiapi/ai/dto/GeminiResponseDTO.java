package com.example.aiapi.ai.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GeminiResponseDTO {
    private List<Step> steps;

    @Getter
    @NoArgsConstructor
    public static class Step {

        private String type;
        private List<Content> content;
    }

    @Getter
    @NoArgsConstructor
    public static class Content {

        private String type;
        private String text;
    }
}
