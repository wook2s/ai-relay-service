package com.example.aiapi.job;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public Mono<Job> create(String prompt) {
        Job job = new Job();
        job.setId(UuidCreator.getTimeOrderedEpoch().toString());
        job.setPrompt(prompt);
        job.setStatus(JobStatus.QUEUED);
        job.setRetryCount(0);
        job.setCreatedAt(LocalDateTime.now());
        return jobRepository.save(job);
    }
}
