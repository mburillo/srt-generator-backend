package com.soundintotext.srt_generator.domain.model;

public record ErrorResponse(String code, String message, String details) {}