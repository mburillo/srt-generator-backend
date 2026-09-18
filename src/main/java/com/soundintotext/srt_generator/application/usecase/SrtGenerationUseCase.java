package com.soundintotext.srt_generator.application.usecase;

import com.soundintotext.srt_generator.domain.model.Job;
import org.springframework.web.multipart.MultipartFile;

public interface SrtGenerationUseCase {
    Job generateSrt(MultipartFile file,
                    String targetLanguage);
}
