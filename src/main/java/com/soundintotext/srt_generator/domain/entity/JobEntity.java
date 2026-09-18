package com.soundintotext.srt_generator.domain.entity;

import com.soundintotext.srt_generator.domain.model.JobStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "jobs")
public class JobEntity {

    @Id
    private String jobId;

    @Column(nullable = false)
    private String objectKey;

    private String targetLanguage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    private String detectedLanguage;
    private String srtObjectKey;

    @Column(length = 2000)
    private String error;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant completedAt;

    public JobEntity() {} // requerido por JPA

    // getters y setters de todos los campos
    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }
    public String getObjectKey() { return objectKey; }
    public void setObjectKey(String objectKey) { this.objectKey = objectKey; }
    public String getTargetLanguage() { return targetLanguage; }
    public void setTargetLanguage(String targetLanguage) { this.targetLanguage = targetLanguage; }
    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }
    public String getDetectedLanguage() { return detectedLanguage; }
    public void setDetectedLanguage(String detectedLanguage) { this.detectedLanguage = detectedLanguage; }
    public String getSrtObjectKey() { return srtObjectKey; }
    public void setSrtObjectKey(String srtObjectKey) { this.srtObjectKey = srtObjectKey; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}