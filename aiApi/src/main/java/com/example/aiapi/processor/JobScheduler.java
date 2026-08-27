package com.example.aiapi.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobScheduler {

    private final JobWorker jobWorker;

    @Scheduled(fixedDelay = 30000)
    public void processJobs() {
        jobWorker.processQueuedJobs().subscribe();
    }
}
