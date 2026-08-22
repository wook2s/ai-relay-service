package com.example.aiapi.job;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface JobRepository extends ReactiveCrudRepository<Job, String> {

    Flux<Job> findByStatus(JobStatus status);
}
