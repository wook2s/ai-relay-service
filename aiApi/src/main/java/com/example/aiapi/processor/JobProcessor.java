package com.example.aiapi.processor;

import com.example.aiapi.ai.AiClient;
import com.example.aiapi.job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JobProcessor {

    private final JobService jobService;

    private final AiClient aiClient;

    public JobProcessor(
            JobService jobService,
            @Qualifier("mockAiClient") AiClient aiClient
    ) {
        this.jobService = jobService;
        this.aiClient = aiClient;
    }

    public Mono<Void> process(String id) {
        return jobService.startJob(id)
                .flatMap(job -> {
                    System.out.println("START job = " + System.identityHashCode(job));
                    System.out.println("START retryCount = " + job.getRetryCount());

                    return aiClient.generate(job.getPrompt())
                            .flatMap(result ->
                                    jobService.completeJob(id, result)
                            )
                            .onErrorResume(error -> {
                                System.out.println("ERROR job = " + System.identityHashCode(job));
                                System.out.println("ERROR retryCount = " + job.getRetryCount());
                                System.out.println("ERROR = " + error);

                                if (job.getRetryCount() < 3) {
                                    return jobService.retryJob(id);
                                }

                                return jobService.failJob(id);
                            });
                })
                .then();
    }
}
