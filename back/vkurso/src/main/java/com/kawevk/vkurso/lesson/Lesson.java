package com.kawevk.vkurso.lesson;

import com.kawevk.vkurso.module.Module;
import com.kawevk.vkurso.shared.persistence.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Entity
@Table(name = "lessons")
@Getter @Setter
public class Lesson extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "duration_seconds", nullable = false)
    private Duration durationSeconds;

    @Column(name = "free_preview", nullable = false)
    private boolean freePreview = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    protected Lesson() {}

    public Lesson(String title, String description, int orderIndex, String videoUrl, int durationSeconds) {
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
        this.videoUrl = videoUrl;
        this.durationSeconds = Duration.ofSeconds(durationSeconds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
