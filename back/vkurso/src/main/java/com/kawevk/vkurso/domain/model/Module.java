package com.kawevk.vkurso.domain.model;

import com.kawevk.vkurso.domain.exception.InvalidModuleDataException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Module {
    private Long id;
    private String title;
    private String description;
    private Integer position;
    private final List<Lesson> lessons;
    private final Long courseId;
    private final Instant createdAt;
    private Instant updatedAt;

    //------------
    //Construtores
    //------------

    //Construtor para reconstruir, usar o que já veio do banco sem validar pois já existiu antes
    public Module(Long id, String title, String description, Integer position, List<Lesson> lessons, Long courseId, Instant createdAt,  Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.position = position;
        this.lessons = lessons != null ? new ArrayList<>(lessons) : new ArrayList<>();
        this.courseId = courseId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //Construtor para criar o módulo
    private Module(String title, String description, Integer position, Long courseId) {
        validateTitle(title);
        validateCourse(courseId);
        validatePosition(position);

        this.title = title.trim();
        this.description = description != null ? description.trim() : null;
        this.position = position;
        this.courseId = courseId;
        this.lessons = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    //um módulo começa como rascunho
    public static Module createDraft(String title, String description, Integer position, Long courseId) {
        return new Module(title, description, position, courseId);
    }

    //Regras de Negócio

    public void addLesson(Lesson lesson) {
        Objects.requireNonNull(lesson, "lesson não pode ser nula");
        this.lessons.add(lesson);
        touch();
    }

    public void removeLesson(Lesson lesson) {
        Objects.requireNonNull(lesson, "lesson não pode ser nula");
        this.lessons.remove(lesson);
        touch();
    }

    public void updateTitle(String title) {
        validateTitle(title);
        this.title = title.trim();
        touch();
    }

    public void updatePosition(Integer position) {
        validatePosition(position);
        this.position = position;
        touch();
    }

    public void updateDetails(String title, String description) {
        validateTitle(title);
        this.title = title.trim();
        this.description = description != null ? description.trim() : null;
        touch();
    }

    public void moveTo(int position) {
        validatePosition(position);
        this.position = position;
        touch();
    }

    //consultas

    public boolean hasLessons() {
        return !lessons.isEmpty();
    }

    public Integer lessonCount() {
        return this.lessons.size();
    }

    public long totalDurationSeconds() {
        return lessons.stream().mapToLong(Lesson::getDurationSeconds).sum();
    }


    //helpers

    private void touch() {
        this.updatedAt = Instant.now();
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidModuleDataException("Título do módulo é obrigatório");
        }
        if (title.trim().length() > 120) {
            throw new InvalidModuleDataException("Título excede 120 caracteres");
        }
    }

    private static void validatePosition(int position) {
        if (position < 1) {
            throw new InvalidModuleDataException("Posição do módulo deve ser maior ou igual a 1");
        }
    }

    private static void validateCourse(Long courseId) {
        if (courseId == null) {
            throw new InvalidModuleDataException("courseId do módulo é obrigatório");
        }
        if (courseId < 1) {
            throw new InvalidModuleDataException("courseId do módulo deve ser maior que zero");
        }
    }

    // -------
    // Getters
    // -------

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Integer getPosition() { return position; }
    public Long getCourseId() { return courseId; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public List<Lesson> getLessons() {
        return Collections.unmodifiableList(lessons);
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id já atribuído");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return Objects.equals(getId(), module.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
