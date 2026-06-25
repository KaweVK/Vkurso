package com.kawevk.vkurso.course;

import com.kawevk.vkurso.module.Module;
import com.kawevk.vkurso.shared.persistence.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter @Setter
public class Course extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 200, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CourseLevel level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CourseStatus status;

    @Column(name = "instructor_id", nullable = false)
    private Long instructorId;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<Module> modules = new ArrayList<>();

    protected Course() {}

    public Course(String title, String description, CourseLevel level, Long instructorId) {
        this.title = title;
        this.slug = toSlug(title);
        this.description = description;
        this.level = level;
        this.status = CourseStatus.DRAFT;
        this.instructorId = instructorId;
    }

    //Métodos de negócio

    public void addModule(Module module) {
        modules.add(module);
        module.setCourse(this);
    }

    public void removeModule(Module module) {
        modules.remove(module);
        module.setCourse(null);
    }

    public String toSlug(String input) {
        String semAcento = Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("\\p{M}", ""); // remove acentos
        return semAcento.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // tira o que não é letra/número
                .replaceAll("\\s+", "-") // espaço vira hífen
                .replaceAll("-{2,}", "-") // colapsa hífens repetidos
                .replaceAll("^-|-$", ""); // tira hífen das pontas
    }

    public void publish() {
        if (modules.isEmpty()) {
            throw new IllegalStateException("Um curso não pode ser publicado sem módulos.");
        }
        this.status = CourseStatus.PUBLISHED;
    }

    public void archive() {
        this.status = CourseStatus.ARCHIVED;
    }

    public boolean isPublished() {
        return status == CourseStatus.PUBLISHED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
