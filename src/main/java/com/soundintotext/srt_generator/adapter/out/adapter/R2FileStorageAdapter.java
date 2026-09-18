package com.soundintotext.srt_generator.adapter.out.adapter;

import com.soundintotext.srt_generator.adapter.out.port.FileStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.net.URI;

@Component
@ConditionalOnProperty(prefix = "storage", name = "provider", havingValue = "r2")
public class R2FileStorageAdapter implements FileStoragePort {

    private final S3Client s3Client;
    private final String bucket;

    public R2FileStorageAdapter(
            @Value("${storage.r2.endpoint}") String endpoint,
            @Value("${storage.r2.access-key}") String accessKey,
            @Value("${storage.r2.secret-key}") String secretKey,
            @Value("${storage.r2.bucket}") String bucket) {

        this.bucket = bucket;
        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of("auto"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .build();
    }

    @Override
    public String store(String jobId, String originalFilename, InputStream content, long size, String contentType) {
        String objectKey = "uploads/%s/source%s".formatted(jobId, extractExtension(originalFilename));
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(objectKey)
                        .contentType(contentType)
                        .contentLength(size)
                        .build(),
                RequestBody.fromInputStream(content, size)
        );

        return objectKey;
    }

    @Override
    public InputStream retrieve(String objectKey) {
        return s3Client.getObject(GetObjectRequest.builder().bucket(bucket).key(objectKey).build());
    }

    private String extractExtension(String filename) {
        if (filename == null) return "";
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex >= 0 ? filename.substring(dotIndex) : "";
    }
}