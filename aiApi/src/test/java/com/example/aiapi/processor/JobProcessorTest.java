package com.example.aiapi.processor;

import com.example.aiapi.job.Job;
import com.example.aiapi.job.JobService;
import com.example.aiapi.job.JobStatus;
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
public class JobProcessorTest {
    @Mock
    private JobService jobService;

    @InjectMocks
    private JobProcessor jobProcessor;

    @Test
    void processJob() {
        String id = "20260822-test";

        Job saved = new Job();
        saved.setId(id);
        saved.setPrompt("테스트");
        saved.setStatus(JobStatus.QUEUED);
        saved.setRetryCount(0);

        when(jobService.startJob(id))
            .thenReturn(Mono.just(saved));

        StepVerifier.create(jobProcessor.process(id))
            .verifyComplete();

        verify(jobService).startJob(id);
    }

}
