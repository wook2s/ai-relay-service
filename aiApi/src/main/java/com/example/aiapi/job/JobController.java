package com.example.aiapi.job;

import com.example.aiapi.job.dto.JobCreateRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JobController {
    private final JobService jobService;

    @PostMapping("/api/jobs")
    public Mono<Job> create(@RequestBody JobCreateRequestDTO requestDTO) {
        return jobService.create(requestDTO.prompt());
    }

    @GetMapping("/api/jobs/{id}")
    public Mono<Job> findById(@PathVariable String id) {
        return jobService.findById(id);
    }

    @PostMapping("/api/jobs/{id}/start")
    public Mono<Job> start(@PathVariable String id) {
        return jobService.startJob(id);
    }
}
