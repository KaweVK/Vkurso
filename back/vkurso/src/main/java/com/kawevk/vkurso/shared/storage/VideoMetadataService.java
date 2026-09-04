package com.kawevk.vkurso.shared.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class VideoMetadataService {

    public int getDuration(MultipartFile video) {
        try {

        File file = File.createTempFile("video", ".mp4");
        video.transferTo(file);

        Process process = new ProcessBuilder(
                "ffprobe",
                "-v", "error",
                "-show_entries", "format=duration",
                "-of", "csv=p=0",
                file.getAbsolutePath()
        ).start();

        String result = new String(
                process.getInputStream().readAllBytes()
        ).trim();

        file.delete();

        return (int) Math.round(Double.parseDouble(result));
        } catch (Exception e) {
            throw new StorageException("Impossibilitade de retornar duração", e);
        }
    }
}
