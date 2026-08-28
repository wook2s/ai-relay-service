package com.example.aiapi.ai;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component("mockAiClient")
public class MockAiClient implements AiClient{

    @Override
    public Mono<String> generate(String prompt) {

        int delay = (int) (Math.random() * 6) + 5;
        return Mono.just("Mock Ai 결과 >> " + prompt).delayElement(Duration.ofSeconds(delay));
    }
}
