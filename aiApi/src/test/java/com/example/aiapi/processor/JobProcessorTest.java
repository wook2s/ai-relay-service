package com.example.aiapi.processor;

import com.example.aiapi.ai.AiClient;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobProcessorTest {
    @Mock
    private JobService jobService;
    @Mock
    private AiClient aiClient;

    @InjectMocks
    private JobProcessor jobProcessor;

    @Test
    void processJobSuccess() {
        String id = "20260822-test";

        Job processingJob = new Job();
        processingJob.setId(id);
        processingJob.setPrompt("테스트 프롬프트");
        processingJob.setStatus(JobStatus.PROCESSING);

        Job completedJob = new Job();
        completedJob.setId(id);
        completedJob.setPrompt("테스트 프롬프트");
        completedJob.setStatus(JobStatus.COMPLETED);
        completedJob.setResult("AI 결과");

        when(jobService.startJob(id))
                .thenReturn(Mono.just(processingJob));

        when(aiClient.generate("테스트 프롬프트"))
                .thenReturn(Mono.just("AI 결과"));

        when(jobService.completeJob(id, "AI 결과"))
                .thenReturn(Mono.just(completedJob));

        StepVerifier.create(jobProcessor.process(id))
                .verifyComplete();

        verify(jobService).startJob(id);
        verify(aiClient).generate("테스트 프롬프트");
        verify(jobService).completeJob(id, "AI 결과");
    }

    @Test
    void processJobFail() {
        String id = "job-2";

        Job processingJob = new Job();
        processingJob.setId(id);
        processingJob.setPrompt("실패 테스트");
        processingJob.setStatus(JobStatus.PROCESSING);

        Job failedJob = new Job();
        failedJob.setId(id);
        failedJob.setPrompt("실패 테스트");
        failedJob.setStatus(JobStatus.FAILED);

        when(jobService.startJob(id))
                .thenReturn(Mono.just(processingJob));
        when(aiClient.generate("실패 테스트"))
                .thenReturn(Mono.error(new RuntimeException("AI 호출 실패")));
        when(jobService.failJob(id))
                .thenReturn(Mono.just(failedJob));

        StepVerifier.create(jobProcessor.process(id)).verifyComplete();

        verify(jobService).startJob(id);
        verify(aiClient).generate("실패 테스트");
        verify(jobService).failJob(id);
        verify(jobService, never()).completeJob(eq(id), anyString());
    }
}
