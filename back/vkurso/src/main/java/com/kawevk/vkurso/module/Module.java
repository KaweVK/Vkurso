package com.kawevk.vkurso.module;

import com.kawevk.vkurso.course.Course;
import com.kawevk.vkurso.lesson.Lesson;
import com.kawevk.vkurso.shared.persistence.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modules")
@Getter
@Setter
public class Module extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "order_index", nullable = false)
    private Long orderIndex;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<Lesson> lessons = new ArrayList<>();

    protected Module() {}

    public Module(String title, String description, Long orderIndex) {
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
    }

    //Métodos de negócio

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.setModule(this);
    }

    public void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
        lesson.setModule(null);
    }

    public void changeLessonOrder(Long lessonId, Long newOrder) {
        Lesson target = lessons.stream()
                .filter(l -> l.getId() != null && l.getId().equals(lessonId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Lição não pertence a este curso: " + lessonId));

        lessons.remove(target);
        int destino = Math.max(0, Math.min(newOrder.intValue(), lessons.size()));
        lessons.add(destino, target);

        for (int i = 0; i < lessons.size(); i++) {
            lessons.get(i).setOrderIndex((long) i);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Module other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
