package com.soundintotext.srt_generator.domain.exception;

public class JobNotReadyException extends RuntimeException {
    public JobNotReadyException(String jobId, String currentStatus) {
        super("El job %s no está listo todavía (estado actual: %s)".formatted(jobId, currentStatus));
    }
}
