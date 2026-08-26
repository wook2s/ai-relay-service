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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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

        given(jobService.findQueuedJobs())
                .willReturn(Flux.just(job1, job2));

        given(jobProcessor.process("job-1"))
                .willReturn(Mono.empty());
        given(jobProcessor.process("job-2"))
                .willReturn(Mono.empty());

        StepVerifier.create(jobWorker.processQueuedJobs()).verifyComplete();

        then(jobProcessor).should().process("job-1");
        then(jobProcessor).should().process("job-2");
    }
}
