package com.example.aiapi.job;

import com.example.aiapi.job.dto.JobResponseDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JobEventPublisher {
    private final Map<String, Sinks.Many<JobResponseDTO>> sinks = new ConcurrentHashMap<>();

    public void publish(String id, JobResponseDTO jobResponseDTO) {
        Sinks.Many<JobResponseDTO> sink = sinks.get(id);

        if (sink != null) {
            sink.tryEmitNext(jobResponseDTO);
        }
    }

    public Flux<JobResponseDTO> subscribe(String id) {
        Sinks.Many<JobResponseDTO> sink =
                sinks.computeIfAbsent(id, key -> Sinks.many().multicast().onBackpressureBuffer());
        return sink.asFlux();
    }


}
