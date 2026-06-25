package com.kawevk.vkurso.domain.model;

import com.kawevk.vkurso.domain.exception.InvalidLessonDataException;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class Lesson {
    private Long id;
    private String title;
    private String description;
    private Long moduleId;
    private Integer position;
    private Long durationSeconds;
    private String videoUrl;
    private final Instant createdAt;
    private Instant updatedAt;

    //------------
    //Construtores
    //------------

    public Lesson(Long id, String title, String description, Long moduleId, Integer position, Long durationSeconds, String videoUrl, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.moduleId = moduleId;
        this.position = position;
        this.durationSeconds = durationSeconds;
        this.videoUrl = videoUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private Lesson(String title, String description, Long moduleId, Integer position, Long durationSeconds, String videoUrl) {
        this.title = title;
        this.description = description;
        this.moduleId = moduleId;
        this.position = position;
        this.durationSeconds = durationSeconds;
        this.videoUrl = videoUrl;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public static Lesson createDraft(String title, String description, Long moduleId, Integer position, Long durationSeconds, String videoUrl) {
        return new Lesson(title, description, moduleId, position, durationSeconds, videoUrl);
    }

    //-----------------
    //Regras de negócio
    //-----------------

    public void updateDetails(String title, String description) {
        validateTitle(title);
        this.title = title.trim();
        this.description = description != null ? description.trim() : null;
        touch();
    }

    public void updateVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        touch();
    }

    public void updateDurationSeconds(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
        touch();
    }

    public void moveTo(Integer position) {
        this.position = position;
        touch();
    }

    //consultas
    public boolean hasVideoUrl(String videoUrl) {
        return this.videoUrl != null && this.videoUrl.equals(videoUrl);
    }

    //helpers

    private void touch() {
        this.updatedAt = Instant.now();
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidLessonDataException("Título da lição é obrigatório");
        }
        if (title.trim().length() > 120) {
            throw new InvalidLessonDataException("Título excede 120 caracteres");
        }
    }

    private void validateDurationSeconds(Long durationSeconds) {
        if (durationSeconds == null) {
            throw new InvalidLessonDataException("Duração da lição é obrigatória");
        }
        if (durationSeconds <= 0) {
            throw new InvalidLessonDataException("Duração da lição deve ser maior que zero");
        }
    }

    public void validatePosition(Integer position) {
        if (position < 1) {
            throw new InvalidLessonDataException("Posição da lição deve ser maior ou igual a 1");
        }
    }

    public void validateModuleId(Long moduleId) {
        if (moduleId == null) {
            throw new InvalidLessonDataException("ID do módulo é obrigatório");
        }
        if (moduleId < 1) {
            throw new InvalidLessonDataException("ID do módulo deve ser maior que zero");
        }
    }

    // -------
    // Getters
    // -------


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public Integer getPosition() {
        return position;
    }

    public Long getDurationSeconds() {
        return durationSeconds;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(getId(), lesson.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
