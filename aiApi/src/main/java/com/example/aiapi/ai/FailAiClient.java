package com.example.aiapi.ai;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("failAiClient")
public class FailAiClient implements AiClient{
    @Override
    public Mono<String> generate(String prompt) {
        return Mono.error(new RuntimeException("AI 호출 실패"));
    }
}
