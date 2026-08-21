package com.example.aiapi.job;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table("jobs")
public class Job {
    @Id
    private String id;

    private String prompt;
    private JobStatus status;
    private String result;
    private Integer retryCount;

    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
