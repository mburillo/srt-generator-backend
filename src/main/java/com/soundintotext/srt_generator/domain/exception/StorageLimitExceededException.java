package com.soundintotext.srt_generator.domain.exception;

public class StorageLimitExceededException extends RuntimeException {
    public StorageLimitExceededException() {
        super("Se ha superado el limite de almacenamiento");
    }
}
