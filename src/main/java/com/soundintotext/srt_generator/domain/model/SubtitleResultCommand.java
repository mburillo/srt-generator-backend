package com.soundintotext.srt_generator.domain.model;

public record SubtitleResultCommand(
        String jobId,
        String status,
        String srtObjectKey,
        String detectedLanguage,
        String error
) {}
