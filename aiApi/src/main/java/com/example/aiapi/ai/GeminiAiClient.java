package com.example.aiapi.ai;

import com.example.aiapi.ai.dto.GeminiRequestDTO;
import com.example.aiapi.ai.dto.GeminiResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component("geminiAiClient")
public class GeminiAiClient implements AiClient{

    private final WebClient webClient;
    private final String apiKey;

    public GeminiAiClient(
            WebClient geminiWebClient,
            @Value("${gemini.api-key}") String apiKey
    ) {
        this.webClient = geminiWebClient;
        this.apiKey = apiKey;
    }

    @Override
    public Mono<String> generate(String prompt) {
        GeminiRequestDTO request =
                new GeminiRequestDTO("gemini-3.6-flash", prompt);

        return webClient.post()
                .uri("/v1beta/interactions")
                .header("x-goog-api-key", apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GeminiResponseDTO.class)
                .map(response -> response.getSteps().stream()
                        .filter(step -> "model_output".equals(step.getType()))
                        .findFirst()
                        .orElseThrow()
                        .getContent().get(0).getText())
                .timeout(Duration.ofSeconds(30));
    }
}
