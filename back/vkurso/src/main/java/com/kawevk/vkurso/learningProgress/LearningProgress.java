package com.kawevk.vkurso.learningProgress;

import com.kawevk.vkurso.shared.persistence.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "learning_progress",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_learning_progress_student_lesson",
                        columnNames = {"student_id", "lesson_id"}
                )
        })
@Getter @Setter
public class LearningProgress extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "student_id", nullable = false)
    private Long studentId;
    @Column(name = "course_id", nullable = false)
    private Long courseId;
    @Column(name = "module_id")
    private Long moduleId;
    @Column(name = "lesson_id")
    private Long lessonId;

    protected LearningProgress() {}

    public LearningProgress(Long studentId, Long courseId, Long moduleId, Long lessonId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.moduleId = moduleId;
        this.lessonId = lessonId;
    }

    public LearningProgress(Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LearningProgress other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
