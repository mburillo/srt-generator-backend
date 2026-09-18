package com.soundintotext.srt_generator.domain.model;

import java.time.Instant;
import java.time.OffsetDateTime;

public class Job {

    private final String jobId;
    private final String objectKey;
    private final String targetLanguage;
    private JobStatus status;
    private String detectedLanguage;
    private String srtObjectKey;
    private String error;
    private final Instant createdAt;
    private Instant completedAt;

    public Job(String jobId, String objectKey, String targetLanguage, JobStatus status,
               String detectedLanguage, String srtObjectKey, String error,
               Instant createdAt, Instant completedAt) {
        this.jobId = jobId;
        this.objectKey = objectKey;
        this.targetLanguage = targetLanguage;
        this.status = status;
        this.detectedLanguage = detectedLanguage;
        this.srtObjectKey = srtObjectKey;
        this.error = error;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    public static Job createPending(String jobId, String objectKey, String targetLanguage) {
        return new Job(jobId, objectKey, targetLanguage, JobStatus.PENDING,
                null, null, null, Instant.now(), null);
    }

    /** Reconstruye un Job desde persistencia. Solo debe usarlo el adapter de salida. */
    public static Job reconstitute(String jobId, String objectKey, String targetLanguage, JobStatus status,
                                   String detectedLanguage, String srtObjectKey, String error,
                                   Instant createdAt, Instant completedAt) {
        return new Job(jobId, objectKey, targetLanguage, status,
                detectedLanguage, srtObjectKey, error, createdAt, completedAt);
    }

    public void markCompleted(String srtObjectKey, String detectedLanguage) {
        if (this.status == JobStatus.COMPLETED || this.status == JobStatus.FAILED) {
            throw new IllegalStateException("El job %s ya está en estado final: %s".formatted(jobId, status));
        }
        this.status = JobStatus.COMPLETED;
        this.srtObjectKey = srtObjectKey;
        this.detectedLanguage = detectedLanguage;
        this.completedAt = Instant.now();
    }

    public void markFailed(String error) {
        if (this.status == JobStatus.COMPLETED || this.status == JobStatus.FAILED) {
            throw new IllegalStateException("El job %s ya está en estado final: %s".formatted(jobId, status));
        }
        this.status = JobStatus.FAILED;
        this.error = error;
        this.completedAt = Instant.now();
    }

    public void markProcessing() {
        this.status = JobStatus.PROCESSING;
    }

    public String getJobId() { return jobId; }
    public String getObjectKey() { return objectKey; }
    public String getTargetLanguage() { return targetLanguage; }
    public JobStatus getStatus() { return status; }
    public String getDetectedLanguage() { return detectedLanguage; }
    public String getSrtObjectKey() { return srtObjectKey; }
    public String getError() { return error; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getCompletedAt() { return completedAt; }
}