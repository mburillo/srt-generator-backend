package com.soundintotext.srt_generator.adapter.out.adapter;

import com.soundintotext.srt_generator.adapter.out.port.R2StoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
public class R2StorageAdapter implements R2StoragePort {


    private final S3Client s3Client;
    private final String bucket;

    public R2StorageAdapter(
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
    public List<S3Object> listAllObjects() {

        List<S3Object> objects = new ArrayList<>();

        String continuationToken = null;

        do {
            ListObjectsV2Request.Builder requestBuilder =
                    ListObjectsV2Request.builder()
                            .bucket(bucket)
                            .maxKeys(1000);

            if (continuationToken != null) {
                requestBuilder.continuationToken(continuationToken);
            }

            ListObjectsV2Response response =
                    s3Client.listObjectsV2(requestBuilder.build());

            objects.addAll(response.contents());

            continuationToken = response.nextContinuationToken();

        } while (continuationToken != null);

        return objects;
    }

    @Override
    public BigDecimal getTotalSize() {
        List<S3Object> allObjects = listAllObjects();
        BigDecimal totalSize = BigDecimal.ZERO;
        for (S3Object object : allObjects) {
            totalSize = totalSize.add(BigDecimal.valueOf(object.size()));
        }
        return totalSize;
    }

    public void delete(String objectKey) {

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();

        s3Client.deleteObject(request);
    }
}
