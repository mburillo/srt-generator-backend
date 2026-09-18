package com.soundintotext.srt_generator.domain.exception;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(String jobId) {
        super("No se encontró el job: " + jobId);
    }
}