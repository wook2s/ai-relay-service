package com.example.aiapi.ai;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("mockAiClient")
public class MockAiClient implements AiClient{

    @Override
    public Mono<String> generate(String prompt) {
        return Mono.just("Ai 결과 + " + prompt);
    }
}
