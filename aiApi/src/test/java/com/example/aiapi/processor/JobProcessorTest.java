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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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

        given(jobService.startJob(id))
                .willReturn(Mono.just(processingJob));

        given(aiClient.generate("테스트 프롬프트"))
                .willReturn(Mono.just("AI 결과"));

        given(jobService.completeJob(id, "AI 결과"))
                .willReturn(Mono.just(completedJob));

        StepVerifier.create(jobProcessor.process(id))
                .verifyComplete();

        then(jobService).should().startJob(id);
        then(aiClient).should().generate("테스트 프롬프트");
        then(jobService).should().completeJob(id, "AI 결과");
    }

    @Test
    void processJobFail() {
        String id = "job-2";

        Job processingJob = new Job();
        processingJob.setId(id);
        processingJob.setPrompt("실패 테스트");
        processingJob.setStatus(JobStatus.PROCESSING);
        processingJob.setRetryCount(3);
        Job failedJob = new Job();
        failedJob.setId(id);
        failedJob.setPrompt("실패 테스트");
        failedJob.setStatus(JobStatus.FAILED);

        given(jobService.startJob(id))
                .willReturn(Mono.just(processingJob));
        given(aiClient.generate("실패 테스트"))
                .willReturn(Mono.error(new RuntimeException("AI 호출 실패")));
        given(jobService.failJob(id))
                .willReturn(Mono.just(failedJob));

        StepVerifier.create(jobProcessor.process(id)).verifyComplete();

        then(jobService).should().startJob(id);
        then(aiClient).should().generate("실패 테스트");
        then(jobService).should().failJob(id);
        then(jobService).should(never()).completeJob(eq(id), anyString());
    }

    @Test
    void processRetry() {
        String id = "job_2";

        Job processingJob = new Job();
        processingJob.setId(id);
        processingJob.setPrompt("실패 테스트");
        processingJob.setStatus(JobStatus.PROCESSING);
        processingJob.setRetryCount(0);
        System.out.println(processingJob.getRetryCount());

        Job retryJob = new Job();
        retryJob.setId(id);
        retryJob.setPrompt("실패 테스트");
        retryJob.setStatus(JobStatus.QUEUED);
        retryJob.setRetryCount(1);


        System.out.println("processingJob = " + processingJob);
        System.out.println("retryCount = " + processingJob.getRetryCount());

        given(jobService.startJob(id))
                .willReturn(Mono.just(processingJob));

        given(aiClient.generate("실패 테스트"))
                .willReturn(Mono.error(new RuntimeException("AI 호출 실패")));

        given(jobService.retryJob(id))
                .willReturn(Mono.just(retryJob));

        StepVerifier.create(jobProcessor.process(id))
                .verifyComplete();

        then(aiClient).should().generate("실패 테스트");
        then(jobService).should().retryJob(id);
        then(jobService).should(never()).completeJob(anyString(), anyString());
        then(jobService).should(never()).failJob(anyString());
    }

}
