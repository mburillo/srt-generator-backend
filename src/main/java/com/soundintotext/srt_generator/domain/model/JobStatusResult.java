package com.soundintotext.srt_generator.domain.model;

import java.time.Instant;

public record JobStatusResult(
        String jobId,
        JobStatus status,
        String detectedLanguage,
        String targetLanguage,
        Instant createdAt,
        Instant completedAt,
        String error
) {}