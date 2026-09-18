package com.soundintotext.srt_generator.adapter;


import com.soundintotext.srt_generator.domain.exception.JobNotFoundException;
import com.soundintotext.srt_generator.domain.exception.JobNotReadyException;
import com.soundintotext.srt_generator.domain.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.http.ResponseEntity;

@RestControllerAdvice
public class SubtitleExceptionHandler {

    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(JobNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("JOB_NOT_FOUND", e.getMessage(), null));
    }

    @ExceptionHandler(JobNotReadyException.class)
    public ResponseEntity<ErrorResponse> handleNotReady(JobNotReadyException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("JOB_NOT_READY", e.getMessage(), null));
    }
}
