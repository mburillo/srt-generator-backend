package com.soundintotext.srt_generator.adapter.out.port;

import java.io.InputStream;

public interface FileStoragePort {
    String store(String jobId, String originalFilename, InputStream content, long size, String contentType);
    InputStream retrieve(String objectKey);
}
