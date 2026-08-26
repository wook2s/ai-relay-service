package com.example.aiapi.job;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    @Test
    void createJob() {
        Job saved = new Job();
        saved.setId("20260821-test");
        saved.setPrompt("등록 테스트 프롬프트");
        saved.setStatus(JobStatus.QUEUED);
        saved.setRetryCount(0);

        given(jobRepository.save(any(Job.class)))
                .willReturn(Mono.just(saved));

        Mono<Job> result = jobService.create("등록 테스트 프롬프트");

        StepVerifier.create(result).assertNext(job -> {
            assertNotNull(job.getId());
            assertEquals("등록 테스트 프롬프트", job.getPrompt());
            assertEquals(JobStatus.QUEUED, job.getStatus());
            assertEquals(0, job.getRetryCount());
        }).verifyComplete();

        then(jobRepository).should().save(any(Job.class));
    }

    @Test
    void findJobById() {
        String id = "20260822-test";

        Job saved = new Job();
        saved.setId(id);
        saved.setPrompt("조회 테스트 프롬프트");
        saved.setStatus(JobStatus.QUEUED);
        saved.setRetryCount(0);

        given(jobRepository.findById(id))
                .willReturn(Mono.just(saved));

        Mono<Job> result = jobService.findById(id);

        StepVerifier.create(result)
                .assertNext(job -> {
                    assertEquals(id, job.getId());
                    assertEquals("조회 테스트 프롬프트", job.getPrompt());
                    assertEquals(JobStatus.QUEUED, job.getStatus());
                    assertEquals(0, job.getRetryCount());
                })
                .verifyComplete();

        then(jobRepository).should().findById(id);
    }

    @Test
    void startJob() {
        String id = "20260822-test";

        Job saved = new Job();
        saved.setId(id);
        saved.setPrompt("변경 테스트 프롬프트");
        saved.setStatus(JobStatus.QUEUED);
        saved.setRetryCount(0);

        given(jobRepository.findById(id))
                .willReturn(Mono.just(saved));

        given(jobRepository.save(any(Job.class)))
                .willAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Job> result = jobService.startJob(id);

        StepVerifier.create(result)
                .assertNext(job -> {
                    assertEquals(JobStatus.PROCESSING, saved.getStatus());
                    assertNotNull(saved.getStartedAt());
                })
                .verifyComplete();

        then(jobRepository).should().findById(id);
        then(jobRepository).should().save(saved);
    }

    @Test
    void findQueuedJobs() {
        String id = "20260822-test";

        Job saved = new Job();
        saved.setId(id);
        saved.setPrompt("테스트");
        saved.setStatus(JobStatus.QUEUED);
        saved.setRetryCount(0);

        given(jobRepository.findByStatus(JobStatus.QUEUED))
                .willReturn(Flux.just(saved));

        StepVerifier.create(jobService.findQueuedJobs())
                .assertNext(job -> {
                    assertEquals("20260822-test", job.getId());
                    assertEquals("테스트", job.getPrompt());
                    assertEquals(JobStatus.QUEUED, job.getStatus());
                    assertEquals(0, job.getRetryCount());
                })
                .verifyComplete();

        then(jobRepository).should().findByStatus(JobStatus.QUEUED);
    }
}
