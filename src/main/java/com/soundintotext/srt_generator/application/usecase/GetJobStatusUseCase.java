package com.soundintotext.srt_generator.application.usecase;

import com.soundintotext.srt_generator.domain.model.JobStatusResult;

public interface GetJobStatusUseCase {
    JobStatusResult getStatus(String jobId);
}