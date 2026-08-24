package com.example.aiapi.processor;

import com.example.aiapi.job.Job;
import com.example.aiapi.job.JobService;
import com.example.aiapi.job.JobStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobWorkerTest {
    @Mock
    private JobService jobService;
    @Mock
    private JobProcessor jobProcessor;

    @InjectMocks
    private JobWorker jobWorker;

    @Test
    void processQueuedJobs() {
        Job job1 = new Job();
        job1.setId("job-1");
        job1.setStatus(JobStatus.QUEUED);

        Job job2 = new Job();
        job2.setId("job-2");
        job2.setStatus(JobStatus.QUEUED);

        when(jobService.findQueuedJobs())
            .thenReturn(Flux.just(job1, job2));

        when(jobProcessor.process("job-1")).thenReturn(Mono.empty());
        when(jobProcessor.process("job-2")).thenReturn(Mono.empty());

        StepVerifier.create(jobWorker.processQueuedJobs()).verifyComplete();

        verify(jobProcessor).process("job-1");
        verify(jobProcessor).process("job-2");
    }
}
