package com.example.aiapi.processor;

import com.example.aiapi.job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JobProcessor {

    private final JobService jobService;

    public Mono<Void> process(String id) {
        return jobService.startJob(id).then();
    }
}
