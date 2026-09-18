package com.soundintotext.srt_generator.domain.model;

import java.time.Instant;

public record SrtGenerationRequest(
        String jobId,
        String objectKey,
        String sourceLanguage,
        String targetLanguage,
        Instant requestedAt
) {}