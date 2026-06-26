package com.kawevk.vkurso.shared.storage;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class VideoStorageService {

    private final MinioClient client;
    private final StorageProperties props;

    public VideoStorageService(MinioClient client, StorageProperties props) {
        this.client = client;
        this.props = props;
    }

    /**
     * Faz upload e retorna a OBJECT KEY (não a URL).
     * Persistir a key na Lesson — nunca a URL completa.
     */
    public String upload(Long courseId, Long lessonId, MultipartFile file) {
        String key = buildKey(courseId, lessonId, file.getOriginalFilename());
        try (InputStream in = file.getInputStream()) {
            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(props.bucket())
                            .object(key)
                            .stream(in, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return key;
        } catch (Exception e) {
            throw new StorageException("Falha ao subir vídeo: " + e.getMessage(), e);
        }
    }

    /**
     * Gera uma URL temporária (presigned GET) a partir da key salva.
     */
    public String presignedGetUrl(String objectKey) {
        try {
            return client.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(props.bucket())
                            .object(objectKey)
                            .expiry(props.presignedTtlSeconds(), TimeUnit.SECONDS)
                            .build()
            );
        } catch (Exception e) {
            throw new StorageException("Falha ao gerar URL do vídeo: " + e.getMessage(), e);
        }
    }

    public void delete(String objectKey) {
        try {
            client.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(props.bucket())
                            .object(objectKey)
                            .build()
            );
        } catch (Exception e) {
            throw new StorageException("Falha ao remover vídeo: " + e.getMessage(), e);
        }
    }

    private String buildKey(Long courseId, Long lessonId, String originalName) {
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf('.'));
        }
        return "courses/%d/lessons/%d/%s%s".formatted(courseId, lessonId, UUID.randomUUID(), ext);
    }
}