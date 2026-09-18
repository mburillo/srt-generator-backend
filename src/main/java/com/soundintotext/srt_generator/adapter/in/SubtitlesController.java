package com.soundintotext.srt_generator.adapter.in;

import com.example.subtitles.api.SubtitlesApi;
import com.example.subtitles.model.JobCreatedResponse;
import com.example.subtitles.model.JobStatusResponse;
import com.soundintotext.srt_generator.application.usecase.DownloadSubtitleUseCase;
import com.soundintotext.srt_generator.application.usecase.GetJobStatusUseCase;
import com.soundintotext.srt_generator.application.usecase.SrtGenerationUseCase;
import com.soundintotext.srt_generator.domain.model.Job;
import com.soundintotext.srt_generator.domain.model.JobStatus;
import com.soundintotext.srt_generator.domain.model.JobStatusResult;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;

@RestController
public class SubtitlesController implements SubtitlesApi {


    private final SrtGenerationUseCase srtGenerationUseCase;
    private final GetJobStatusUseCase getJobStatusUseCase;
    private final DownloadSubtitleUseCase downloadSubtitleUseCase;

    public SubtitlesController(SrtGenerationUseCase srtGenerationUseCase, GetJobStatusUseCase getJobStatusUseCase, DownloadSubtitleUseCase downloadSubtitleUseCase) {
        this.srtGenerationUseCase = srtGenerationUseCase;
        this.getJobStatusUseCase = getJobStatusUseCase;
        this.downloadSubtitleUseCase = downloadSubtitleUseCase;
    }

    @Override
    public ResponseEntity<JobCreatedResponse> createSubtitleJob(MultipartFile file, String targetLanguage) {
        Job job = this.srtGenerationUseCase.generateSrt(file, targetLanguage);
        JobCreatedResponse jobCreatedResponse = new JobCreatedResponse();
        jobCreatedResponse.setJobId(job.getJobId());
        jobCreatedResponse.setStatus(com.example.subtitles.model.JobStatus.fromValue(job.getStatus().toString()));
        return ResponseEntity.ok(jobCreatedResponse);
    }

    @Override
    public ResponseEntity<Resource> downloadSubtitleFile(String jobId) {
        DownloadSubtitleUseCase.SubtitleFile file = downloadSubtitleUseCase.download(jobId);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(file.filename()))
                .body(new InputStreamResource(file.content()));
    }

    @Override
    public ResponseEntity<JobStatusResponse> getSubtitleJobStatus(String jobId) {
        JobStatusResult result = getJobStatusUseCase.getStatus(jobId);

        String downloadUrl = result.status() == JobStatus.COMPLETED
                ? "/api/v1/sdownloadSubtitleFileubtitles/%s/download".formatted(jobId)
                : null;

        JobStatusResponse response = new JobStatusResponse();
        response.setJobId(result.jobId());
        response.setStatus(com.example.subtitles.model.JobStatus.fromValue(result.status().toString()));
        response.setDetectedLanguage(result.detectedLanguage());
        response.setDownloadUrl(downloadUrl);
        response.setError(result.error());

        return ResponseEntity.ok(response);
    }
}
