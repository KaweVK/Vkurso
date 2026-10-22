package com.kawevk.vkurso.module.unit;

import com.kawevk.vkurso.lesson.Lesson;
import com.kawevk.vkurso.module.exceptions.LessonOfOtherModuleException;
import com.kawevk.vkurso.user.Role;
import com.kawevk.vkurso.user.User;
import com.kawevk.vkurso.module.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleTest {
    private User instructor;

    @BeforeEach
    void setUp() {
        instructor = new User(
                "Teste",
                "teste@gmail.com",
                "senha",
                Role.INSTRUCTOR
        );

        instructor.setId(1L);
    }

    @Nested
    @DisplayName("Add e Remove lesson")
    class lesson {
        @Test
        @DisplayName("Add lição")
        void addLessonSuccessfully() {
            Module module = new Module(
                    "Modulo de Teste ",
                    "Modulo de teste",
                    1L
            );
            module.setId(1L);

            Lesson lesson = new Lesson(
              "Lição de Teste",
              "Lição de teste",
              1L
            );
            lesson.setId(1L);

            module.addLesson(lesson);

            assertTrue(module.getLessons().contains(lesson));
            assertEquals(lesson.getModule(), module);
        }

        @Test
        @DisplayName("Add mais de uma lição")
        void addMoreThanOneLessonSuccessfully() {
            Module module = new Module(
                    "Modulo de Teste ",
                    "Modulo de teste",
                    1L
            );
            module.setId(1L);

            Lesson lesson1 = new Lesson(
                    "Lição de Teste 2",
                    "Lição de teste 2",
                    1L
            );
            lesson1.setId(1L);

            Lesson lesson2 = new Lesson(
                    "Lição de Teste 1",
                    "Lição de teste 1",
                    2L
            );
            lesson2.setId(2L);

            module.addLesson(lesson1);
            module.addLesson(lesson2);

            assertTrue(module.getLessons().contains(lesson1));
            assertTrue(module.getLessons().contains(lesson2));
            assertEquals(lesson1.getModule(), module);
            assertEquals(lesson2.getModule(), module);
        }

        @Test
        @DisplayName("Remover lição")
        void removeLesson() {
            Module module = new Module(
                    "Modulo de Teste ",
                    "Modulo de teste",
                    1L
            );
            module.setId(1L);

            Lesson lesson = new Lesson(
                    "Lição de Teste",
                    "Lição de teste",
                    1L
            );
            lesson.setId(1L);

            module.addLesson(lesson);
            module.removeLesson(lesson);

            assertFalse(module.getLessons().contains(lesson));
            assertNull(lesson.getModule());
        }
    }

    @Nested
    @DisplayName("Mudar ordem das lições")
    class changeLessonOrder {
        @Test
        @DisplayName("Muda a ordem corretamente")
        void changeOrder() {
            Module module = new Module(
                    "Modulo de Teste ",
                    "Modulo de teste",
                    1L
            );
            module.setId(1L);

            Lesson lesson1 = new Lesson(
                    "Lição de Teste 2",
                    "Lição de teste 2",
                    1L
            );
            lesson1.setId(1L);

            Lesson lesson2 = new Lesson(
                    "Lição de Teste 2",
                    "Lição de teste 2",
                    2L
            );
            lesson2.setId(2L);

            Lesson lesson3 = new Lesson(
                    "Lição de Teste 3",
                    "Lição de teste 3",
                    3L
            );
            lesson3.setId(3L);

            module.addLesson(lesson1);
            module.addLesson(lesson2);
            module.addLesson(lesson3);

            module.changeLessonOrder(2L, 0L);
            assertEquals(List.of(lesson2, lesson1, lesson3), module.getLessons());
            assertEquals(1L, lesson1.getOrderIndex());
            assertEquals(0L, lesson2.getOrderIndex());
            assertEquals(2L, lesson3.getOrderIndex());

            module.changeLessonOrder(2L, 3L);
            assertEquals(List.of(lesson1, lesson3, lesson2), module.getLessons());
            assertEquals(0L, lesson1.getOrderIndex());
            assertEquals(2L, lesson2.getOrderIndex());
            assertEquals(1L, lesson3.getOrderIndex());

            module.changeLessonOrder(3L, -1L);
            assertEquals(List.of(lesson3, lesson1, lesson2), module.getLessons());
            assertEquals(1L, lesson1.getOrderIndex());
            assertEquals(2L, lesson2.getOrderIndex());
            assertEquals(0L, lesson3.getOrderIndex());
        }

        @Test
        @DisplayName("Lança LessonOfOtherModuleException")
        void changeThrowsLessonOfOtherModuleException() {
            Module module = new Module(
                    "Modulo de Teste ",
                    "Modulo de teste",
                    1L
            );
            module.setId(1L);

            Lesson lesson1 = new Lesson(
                    "Lição de Teste 2",
                    "Lição de teste 2",
                    1L
            );
            lesson1.setId(1L);

            Lesson lesson2 = new Lesson(
                    "Lição de Teste 2",
                    "Lição de teste 2",
                    2L
            );
            lesson2.setId(2L);

            module.addLesson(lesson1);
            module.addLesson(lesson2);

            assertThrows(LessonOfOtherModuleException.class, () -> module.changeLessonOrder(3L, 0L));
        }
    }
}
