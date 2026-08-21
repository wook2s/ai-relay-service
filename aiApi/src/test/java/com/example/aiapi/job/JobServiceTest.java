package com.example.aiapi.job;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    @Test
    void make_job() {
        Job saved = new Job();
        saved.setId("20260821-test");
        saved.setPrompt("테스트 프롬프트");
        saved.setStatus(JobStatus.QUEUED);
        saved.setRetryCount(0);

        when(jobRepository.save(any(Job.class)))
                .thenReturn(Mono.just(saved));

        Mono<Job> result = jobService.create("테스트 프롬프트");

        StepVerifier.create(result).assertNext(job -> {
            assertNotNull(job.getId());
            assertEquals("테스트 프롬프트", job.getPrompt());
            assertEquals(JobStatus.QUEUED, job.getStatus());
            assertEquals(0, job.getRetryCount());
        }).verifyComplete();

        verify(jobRepository).save(any(Job.class));
    }
}
