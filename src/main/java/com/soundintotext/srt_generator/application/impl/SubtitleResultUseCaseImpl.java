package com.soundintotext.srt_generator.application.impl;


import com.example.subtitles.model.JobStatus;
import com.soundintotext.srt_generator.adapter.in.port.JobRepositoryPort;
import com.soundintotext.srt_generator.application.usecase.ProcessSubtitleResultUseCase;
import com.soundintotext.srt_generator.domain.exception.JobNotFoundException;
import com.soundintotext.srt_generator.domain.model.Job;
import com.soundintotext.srt_generator.domain.model.SubtitleResultCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SubtitleResultUseCaseImpl implements ProcessSubtitleResultUseCase {

    private static final Logger log = LoggerFactory.getLogger(SubtitleResultUseCaseImpl.class);

    private final JobRepositoryPort jobRepositoryPort;

    public SubtitleResultUseCaseImpl(JobRepositoryPort jobRepositoryPort) {
        this.jobRepositoryPort = jobRepositoryPort;
    }

    @Override
    public void processResult(SubtitleResultCommand command) {
        Job job = jobRepositoryPort.findById(command.jobId())
                .orElseThrow(() -> new JobNotFoundException(command.jobId()));

        JobStatus status = JobStatus.valueOf(command.status());

        switch (status) {
            case COMPLETED -> job.markCompleted(command.srtObjectKey(), command.detectedLanguage());
            case FAILED -> job.markFailed(command.error());
            default -> throw new IllegalArgumentException(
                    "Estado de resultado inesperado para el job %s: %s".formatted(command.jobId(), status));
        }

        jobRepositoryPort.save(job);
        log.info("Job {} actualizado a estado {}", command.jobId(), status);
    }
}