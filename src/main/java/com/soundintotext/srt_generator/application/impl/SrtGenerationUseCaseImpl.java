package com.soundintotext.srt_generator.application.impl;

import com.soundintotext.srt_generator.adapter.in.port.JobRepositoryPort;
import com.soundintotext.srt_generator.adapter.out.persistence.SpringDataJobRepository;
import com.soundintotext.srt_generator.adapter.out.port.FileStoragePort;
import com.soundintotext.srt_generator.application.service.SrtGenerationProducer;
import com.soundintotext.srt_generator.application.usecase.SrtGenerationUseCase;
import com.soundintotext.srt_generator.domain.entity.JobEntity;
import com.soundintotext.srt_generator.domain.exception.FileStorageException;
import com.soundintotext.srt_generator.domain.model.Job;
import com.soundintotext.srt_generator.domain.model.SrtGenerationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Service
public class SrtGenerationUseCaseImpl implements SrtGenerationUseCase {

    private final SrtGenerationProducer producer;
    private final FileStoragePort fileStoragePort;
    private final JobRepositoryPort jobRepository;

    public SrtGenerationUseCaseImpl(SrtGenerationProducer producer, FileStoragePort fileStoragePort, JobRepositoryPort jobRepository) {
        this.producer = producer;
        this.fileStoragePort = fileStoragePort;
        this.jobRepository = jobRepository;
    }

    @Override
    public Job generateSrt(MultipartFile file, String targetLanguage) {
        String jobId = UUID.randomUUID().toString();
        String objectKey = null;
        try {
            objectKey = fileStoragePort.store(jobId, file.getOriginalFilename(),file.getInputStream(),file.getSize(),file.getContentType());
        } catch (IOException e) {
            throw new FileStorageException(e.getMessage(), e);
        }

        SrtGenerationRequest request = new SrtGenerationRequest(
                jobId, objectKey, null, targetLanguage, Instant.now()
        );
        producer.sendGenerationRequest(request);
        Job job = Job.createPending(jobId,objectKey,targetLanguage);
        jobRepository.save(job);
        return job;
    }
}
