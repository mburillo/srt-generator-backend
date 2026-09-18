package com.soundintotext.srt_generator.adapter.in.port;

import com.soundintotext.srt_generator.domain.model.Job;

import java.util.Optional;

public interface JobRepositoryPort {
    void save(Job job);
    Optional<Job> findById(String jobId);
}