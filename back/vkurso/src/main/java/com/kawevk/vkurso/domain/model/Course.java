package com.kawevk.vkurso.domain.model;

import com.kawevk.vkurso.domain.exception.CourseAlreadyPublishedException;
import com.kawevk.vkurso.domain.exception.CourseArchivedException;
import com.kawevk.vkurso.domain.exception.CourseWithoutContentException;
import com.kawevk.vkurso.domain.exception.InvalidCourseDataException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Course {
    private Long id;
    private String title;
    private String description;
    private final Long instructorId;
    private Status status;
    private String category;
    private String thumbnailUrl;
    private BigDecimal price;
    private final List<Module> modules;
    private final Instant createdAt;
    private Instant updatedAt;

    //Construtores

    public Course(Long id, String title, String description, Long instructorId, Status status, String category, String thumbnailUrl, BigDecimal price, List<Module> modules, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.status = status;
        this.category = category;
        this.thumbnailUrl = thumbnailUrl;
        this.price = price;
        this.modules = modules != null ? new ArrayList<>(modules) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private Course(String title, String description, Long instructorId, BigDecimal price, String category) {
        validateTitle(title);
        validateInstructor(instructorId);
        validatePrice(price);

        this.title = title.trim();
        this.description = description != null ? description.trim() : null;
        this.instructorId = instructorId;
        this.price = price;
        this.category = category;
        this.status = Status.DRAFT;
        this.modules = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public static Course createDraft(String title, String description, Long instructorId, BigDecimal price, String category) {
        return new Course(title, description, instructorId, price, category);
    }

    //Regras de Negócio

    public void publish() {
        if (this.status == Status.ARCHIVED) {
            throw new CourseArchivedException(this.id);
        }
        if (this.status == Status.PUBLISHED) {
            throw new CourseAlreadyPublishedException(this.id);
        }
        if (!hasContent()) {
            throw new CourseWithoutContentException(this.id);
        }
        this.status = Status.PUBLISHED;
        touch();
    }

    public void archive() {
        if (this.status == Status.ARCHIVED) {
            return;
        }
        this.status = Status.ARCHIVED;
        touch();
    }

    public void backToDraft() {
        ensureNotArchived();
        this.status = Status.DRAFT;
        touch();
    }

    public void updateDetails(String title, String description, String category) {
        ensureNotArchived();
        validateTitle(title);
        this.title = title.trim();
        this.description = description != null ? description.trim() : null;
        this.category = category;
        touch();
    }

    public void changePrice(BigDecimal newPrice) {
        ensureNotArchived();
        validatePrice(newPrice);
        this.price = newPrice;
        touch();
    }

    public void changeThumbnail(String thumbnailUrl) {
        ensureNotArchived();
        this.thumbnailUrl = thumbnailUrl;
        touch();
    }

    public void addModule(Module module) {
        Objects.requireNonNull(module, "module não pode ser nulo");
        ensureNotArchived();
        this.modules.add(module);
        touch();
    }

    public void removeModule(Module module) {
        ensureNotArchived();
        this.modules.remove(module);
        touch();
    }

    //Consultas

    public boolean isEnrollable() {
        return this.status == Status.PUBLISHED;
    }

    public boolean isOwnedBy(Long instructorId) {
        return Objects.equals(this.instructorId, instructorId);
    }

    public boolean isPublished() {
        return this.status == Status.PUBLISHED;
    }

    public boolean hasContent() {
        return modules.stream().anyMatch(Module::hasLessons);
    }

    public int totalLessons() {
        return modules.stream().mapToInt(Module::lessonCount).sum();
    }

    //Helpers internos

    private void ensureNotArchived() {
        if (this.status == Status.ARCHIVED) {
            throw new CourseArchivedException(this.id);
        }
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidCourseDataException("Título é obrigatório");
        }
        if (title.trim().length() > 150) {
            throw new InvalidCourseDataException("Título excede 150 caracteres");
        }
    }

    private static void validateInstructor(Long instructorId) {
        if (instructorId == null) {
            throw new InvalidCourseDataException("Curso precisa de um instrutor");
        }
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null || price.signum() < 0) {
            throw new InvalidCourseDataException("Preço não pode ser negativo");
        }
    }

    // Getters

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Long getInstructorId() { return instructorId; }
    public Status getStatus() { return status; }
    public BigDecimal getPrice() { return price; }
    public String getCategory() { return category; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id já atribuído");
        }
        this.id = id;
    }

    // Identidade: cursos são iguais quando têm o mesmo id

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(getId(), course.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}