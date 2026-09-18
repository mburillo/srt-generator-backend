package com.soundintotext.srt_generator.application.impl;

import com.soundintotext.srt_generator.adapter.in.port.JobRepositoryPort;
import com.soundintotext.srt_generator.application.usecase.GetJobStatusUseCase;
import com.soundintotext.srt_generator.domain.exception.JobNotFoundException;
import com.soundintotext.srt_generator.domain.model.Job;
import com.soundintotext.srt_generator.domain.model.JobStatusResult;
import org.springframework.stereotype.Service;

@Service
public class GetJobStatusUseCaseImpl implements GetJobStatusUseCase {

    private final JobRepositoryPort jobRepositoryPort;

    public GetJobStatusUseCaseImpl(JobRepositoryPort jobRepositoryPort) {
        this.jobRepositoryPort = jobRepositoryPort;
    }

    @Override
    public JobStatusResult getStatus(String jobId) {
        Job job = jobRepositoryPort.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        return new JobStatusResult(
                job.getJobId(),
                job.getStatus(),
                job.getDetectedLanguage(),
                job.getTargetLanguage(),
                job.getCreatedAt(),
                job.getCompletedAt(),
                job.getError()
        );
    }
}