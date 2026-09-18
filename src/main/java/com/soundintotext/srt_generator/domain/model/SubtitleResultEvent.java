package com.soundintotext.srt_generator.domain.model;

public record SubtitleResultEvent(
        String jobId,
        String status,
        String srtObjectKey,
        String detectedLanguage,
        String error
) {}