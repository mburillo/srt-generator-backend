package com.soundintotext.srt_generator.application.impl;

import com.soundintotext.srt_generator.adapter.in.port.JobRepositoryPort;
import com.soundintotext.srt_generator.adapter.out.port.FileStoragePort;
import com.soundintotext.srt_generator.application.usecase.DownloadSubtitleUseCase;
import com.soundintotext.srt_generator.domain.exception.JobNotFoundException;
import com.soundintotext.srt_generator.domain.exception.JobNotReadyException;
import com.soundintotext.srt_generator.domain.model.Job;
import com.soundintotext.srt_generator.domain.model.JobStatus;
import org.springframework.stereotype.Service;

@Service
public class DownloadSubtitleUseCaseImpl implements DownloadSubtitleUseCase {

    private final JobRepositoryPort jobRepositoryPort;
    private final FileStoragePort fileStoragePort;

    public DownloadSubtitleUseCaseImpl(JobRepositoryPort jobRepositoryPort, FileStoragePort fileStoragePort) {
        this.jobRepositoryPort = jobRepositoryPort;
        this.fileStoragePort = fileStoragePort;
    }

    @Override
    public SubtitleFile download(String jobId) {
        Job job = jobRepositoryPort.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        if (job.getStatus() != JobStatus.COMPLETED) {
            throw new JobNotReadyException(jobId, job.getStatus().name());
        }

        var content = fileStoragePort.retrieve(job.getSrtObjectKey());
        return new SubtitleFile(content, "subtitles-%s.srt".formatted(jobId));
    }
}
