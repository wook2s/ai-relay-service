package com.example.aiapi.job.dto;

import com.example.aiapi.job.JobStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class JobResponseDTO {
    private String id;
    private String prompt;
    private JobStatus status;
    private String result;
    private Integer retryCount;

    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
