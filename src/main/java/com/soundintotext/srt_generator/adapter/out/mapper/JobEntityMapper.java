package com.soundintotext.srt_generator.adapter.out.mapper;
import com.soundintotext.srt_generator.domain.entity.JobEntity;
import com.soundintotext.srt_generator.domain.model.Job;

public class JobEntityMapper {

    public static JobEntity toEntity(Job job) {
        JobEntity entity = new JobEntity();
        entity.setJobId(job.getJobId());
        entity.setObjectKey(job.getObjectKey());
        entity.setTargetLanguage(job.getTargetLanguage());
        entity.setStatus(job.getStatus());
        entity.setDetectedLanguage(job.getDetectedLanguage());
        entity.setSrtObjectKey(job.getSrtObjectKey());
        entity.setError(job.getError());
        entity.setCreatedAt(job.getCreatedAt());
        entity.setCompletedAt(job.getCompletedAt());
        return entity;
    }

    public static Job toDomain(JobEntity entity) {
        return Job.reconstitute(
                entity.getJobId(), entity.getObjectKey(), entity.getTargetLanguage(), entity.getStatus(),
                entity.getDetectedLanguage(), entity.getSrtObjectKey(), entity.getError(),
                entity.getCreatedAt(), entity.getCompletedAt()
        );
    }
}