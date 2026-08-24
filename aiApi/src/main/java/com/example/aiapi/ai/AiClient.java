package com.example.aiapi.ai;

import reactor.core.publisher.Mono;

public interface AiClient {
    Mono<String> generate(String prompt);
}
