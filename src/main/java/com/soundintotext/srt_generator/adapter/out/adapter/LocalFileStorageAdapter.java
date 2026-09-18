package com.soundintotext.srt_generator.adapter.out.adapter;

import com.soundintotext.srt_generator.adapter.out.port.FileStoragePort;
import com.soundintotext.srt_generator.domain.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class LocalFileStorageAdapter implements FileStoragePort {

    private final Path basePath;

    public LocalFileStorageAdapter(@Value("${storage.local.base-path}") String basePath) {
        this.basePath = Path.of(basePath);
        try {
            Files.createDirectories(this.basePath);
        } catch (IOException e) {
            throw new FileStorageException("No se pudo crear el directorio base de almacenamiento: " + basePath, e);
        }
    }

    @Override
    public String store(String jobId, String originalFilename, InputStream content, long size, String contentType) {
        String relativePath = "uploads/%s/source%s".formatted(jobId, extractExtension(originalFilename));
        Path targetPath = basePath.resolve(relativePath);

        try {
            Files.createDirectories(targetPath.getParent());
            Files.copy(content, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Error al guardar el archivo del job " + jobId, e);
        }

        return relativePath;
    }

    private String extractExtension(String filename) {
        if (filename == null) return "";
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex >= 0 ? filename.substring(dotIndex) : "";
    }

    @Override
    public InputStream retrieve(String objectKey) {
        Path targetPath = basePath.resolve(objectKey);
        try {
            return Files.newInputStream(targetPath);
        } catch (IOException e) {
            throw new FileStorageException("No se pudo leer el archivo: " + objectKey, e);
        }
    }
}