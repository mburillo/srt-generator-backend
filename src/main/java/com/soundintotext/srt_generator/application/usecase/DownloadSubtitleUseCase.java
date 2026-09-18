package com.soundintotext.srt_generator.application.usecase;

import java.io.InputStream;

public interface DownloadSubtitleUseCase {
    SubtitleFile download(String jobId);

    record SubtitleFile(InputStream content, String filename) {}
}
