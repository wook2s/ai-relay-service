package com.example.aiapi.job;

import com.example.aiapi.job.dto.JobResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public JobResponseDTO toResponse(Job job) {
        return JobResponseDTO.builder()
                .id(job.getId())
                .prompt(job.getPrompt())
                .status(job.getStatus())
                .result(job.getResult())
                .retryCount(job.getRetryCount())
                .createdAt(job.getCreatedAt())
                .startedAt(job.getStartedAt())
                .completedAt(job.getCompletedAt())
                .build();
    }
}
