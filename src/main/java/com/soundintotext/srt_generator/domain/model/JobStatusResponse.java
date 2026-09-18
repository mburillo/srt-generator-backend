package com.soundintotext.srt_generator.domain.model;

import java.time.Instant;

public record JobStatusResponse(
        String jobId,
        JobStatus status,
        String detectedLanguage,
        String targetLanguage,
        Instant createdAt,
        Instant completedAt,
        String downloadUrl,
        String error
) {}