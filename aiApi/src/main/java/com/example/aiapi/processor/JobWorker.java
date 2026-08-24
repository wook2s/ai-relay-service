package com.example.aiapi.processor;

import com.example.aiapi.job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class JobWorker {
    private final JobService jobService;
    private final JobProcessor jobProcessor;

    public Flux<Void> processQueuedJobs() {
        return jobService.findQueuedJobs()
                .flatMap(job -> jobProcessor.process(job.getId()));
    }
}
