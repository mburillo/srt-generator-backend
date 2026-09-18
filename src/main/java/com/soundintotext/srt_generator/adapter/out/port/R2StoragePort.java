package com.soundintotext.srt_generator.adapter.out.port;

import software.amazon.awssdk.services.s3.model.S3Object;

import java.math.BigDecimal;
import java.util.List;

public interface R2StoragePort {
    List<S3Object> listAllObjects();
    BigDecimal getTotalSize();
    void delete(String objectKey);
}
