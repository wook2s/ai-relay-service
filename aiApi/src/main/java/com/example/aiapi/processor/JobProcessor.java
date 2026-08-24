package com.example.aiapi.processor;

import com.example.aiapi.ai.AiClient;
import com.example.aiapi.job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JobProcessor {

    private final JobService jobService;

    @Qualifier("mockAiClient")
    private final AiClient aiClient;

    public Mono<Void> process(String id) {
        return jobService.startJob(id)
                .flatMap(job ->
                        aiClient.generate(job.getPrompt())
                                .flatMap(result ->
                                        jobService.completeJob(id, result)
                                )
                                .onErrorResume(error -> {
                                    if (job.getRetryCount() < 3) {
                                        return jobService.retryJob(id);
                                    } else {
                                        return jobService.failJob(id);
                                    }
                                })
                )
                .then();
    }
}
