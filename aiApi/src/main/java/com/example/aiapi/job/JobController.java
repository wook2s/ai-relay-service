package com.example.aiapi.job;

import com.example.aiapi.job.dto.JobCreateRequestDTO;
import com.example.aiapi.job.dto.JobResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JobController {
    private final JobService jobService;
    private final JobMapper jobMapper;

    @PostMapping("/api/jobs")
    public Mono<JobResponseDTO> create(@RequestBody JobCreateRequestDTO requestDTO) {
        return jobService.create(requestDTO.prompt()).map(jobMapper::toResponse);
    }

    @GetMapping("/api/jobs/{id}")
    public Mono<JobResponseDTO> findById(@PathVariable String id) {
        return jobService.findById(id).map(jobMapper::toResponse);
    }

    @PostMapping("/api/jobs/{id}/start")
    public Mono<JobResponseDTO> start(@PathVariable String id) {
        return jobService.startJob(id).map(jobMapper::toResponse);
    }
}
