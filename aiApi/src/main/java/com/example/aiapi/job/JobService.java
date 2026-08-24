package com.example.aiapi.job;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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
        job.setNewEntity(true);
        return jobRepository.save(job);
    }

    public Mono<Job> findById(String id) {
        return jobRepository.findById(id)
                .map(job -> {
                    job.setNewEntity(false);
                    return job;
                });
    }

    public Mono<Job> startJob(String id) {
        return jobRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("job not found" + id)))
                .flatMap(job -> {
                    job.setStatus(JobStatus.PROCESSING);
                    job.setStartedAt(LocalDateTime.now());
                    return jobRepository.save(job);
                });
    }

    public Flux<Job> findQueuedJobs() {
        return jobRepository.findByStatus(JobStatus.QUEUED);
    }

    public Mono<Job> completeJob(String id, String result) {
        return jobRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("job not found +" + id)))
                .flatMap(job -> {
                    job.setStatus(JobStatus.COMPLETED);
                    job.setResult(result);
                    job.setCompletedAt(LocalDateTime.now());
                    return jobRepository.save(job);
                });
    }

    public Mono<Job> failJob(String id) {
        return jobRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("job not found + " + id)))
                .flatMap(job ->  {
                    job.setStatus(JobStatus.FAILED);
                    return jobRepository.save(job);
                });
    }

    public Mono<Job> retryJob(String id) {
        return jobRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("job not found + " + id)))
                .flatMap(job -> {
                    job.setStatus(JobStatus.QUEUED);
                    job.setRetryCount(job.getRetryCount() + 1);
                    return jobRepository.save(job);
                });
    }
}
