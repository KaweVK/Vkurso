package com.kawevk.vkurso.course.unit;

import com.kawevk.vkurso.course.Course;
import com.kawevk.vkurso.course.exceptions.CourseWithoutLessonsException;
import com.kawevk.vkurso.course.exceptions.CourseWithoutModulesException;
import com.kawevk.vkurso.course.exceptions.ModuleOfOtherCourseException;
import com.kawevk.vkurso.courseCategory.CourseCategory;
import com.kawevk.vkurso.lesson.Lesson;
import com.kawevk.vkurso.module.Module;
import com.kawevk.vkurso.course.CourseLevel;
import com.kawevk.vkurso.course.CourseStatus;
import com.kawevk.vkurso.user.Role;
import com.kawevk.vkurso.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

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

    @Test
    @DisplayName("Criar curso")
    void crateCourseSuccessfully() {
        Course course = new Course(
                "Curso de teste",
                "Teste",
                BigDecimal.valueOf(67),
                CourseLevel.ADVANCED,
                instructor.getId()
        );

        course.setId(1L);

        assertEquals(CourseStatus.DRAFT, course.getStatus());
        assertTrue(course.getModules().isEmpty());
        assertTrue(course.getCategoryIds().isEmpty());
    }

    @Nested
    @DisplayName("Add e remove módulo")
    class module {
        @Test
        @DisplayName("Adicionar 1 módulo")
        void addOneModule() {
            Course course = new Course(
                    "Curso de teste",
                    "Curso de teste",
                    BigDecimal.valueOf(67),
                    CourseLevel.ADVANCED,
                    instructor.getId()
            );
            course.setId(1L);

            Module module = new Module(
                    "Modulo de Teste",
                    "Modulo de teste",
                    1L
            );
            module.setId(1L);

            course.addModule(module);

            assertEquals(List.of(module), course.getModules());
            assertEquals(module.getCourse(), course);
        }

        @Test
        @DisplayName("Adicionar mais de um módulo")
        void addMoreThanOneModule() {
            Course course = new Course(
                    "Curso de teste",
                    "Curso de teste",
                    BigDecimal.valueOf(67),
                    CourseLevel.ADVANCED,
                    instructor.getId()
            );
            course.setId(1L);

            Module module1 = new Module(
                    "Modulo de Teste 1",
                    "Modulo de teste 1",
                    1L
            );
            module1.setId(1L);

            Module module2 = new Module(
                    "Modulo de Teste 2",
                    "Modulo de teste 2",
                    2L
            );
            module2.setId(2L);

            course.addModule(module1);
            course.addModule(module2);

            assertEquals(List.of(module1, module2), course.getModules());
        }

        @Test
        @DisplayName("Remover módulo")
        void removeModule() {
            Course course = new Course(
                    "Curso de teste",
                    "Curso de teste",
                    BigDecimal.valueOf(67),
                    CourseLevel.ADVANCED,
                    instructor.getId()
            );
            course.setId(1L);

            Module module = new Module(
                    "Modulo de Teste",
                    "Modulo de teste",
                    1L
            );
            module.setId(1L);

            course.addModule(module);
            course.removeModule(module);

            assertTrue(course.getModules().isEmpty());
            assertNull(module.getCourse());
        }
    }

    @Nested
    @DisplayName("Add e remove categoria")
    class category {
        @Test
        @DisplayName("Adicionar categoria")
        void addCategory() {
            Course course = new Course(
                    "Curso de teste",
                    "Curso de teste",
                    BigDecimal.valueOf(67),
                    CourseLevel.ADVANCED,
                    instructor.getId()
            );
            course.setId(1L);

            CourseCategory category = new CourseCategory(
                    "Categoria de Teste"
            );
            category.setId(1L);

            course.addCategory(category.getId());

            assertEquals(Set.of(category.getId()), course.getCategoryIds());
        }

        @Test
        @DisplayName("Remover categoria")
        void removeCategory() {
            Course course = new Course(
                    "Curso de teste",
                    "Curso de teste",
                    BigDecimal.valueOf(67),
                    CourseLevel.ADVANCED,
                    instructor.getId()
            );
            course.setId(1L);

            CourseCategory category = new CourseCategory(
                    "Categoria de Teste"
            );
            category.setId(1L);

            course.addCategory(category.getId());
            course.removeCategory(category.getId());

            assertTrue(course.getCategoryIds().isEmpty());
        }
    }

    @Nested
    @DisplayName("Publicar curso")
    class publish {
        @Test
        @DisplayName("Deve publicar o curso com sucesso")
        void shouldChangeStatusToPublished() {
            Course course = new Course(
                    "Curso de teste",
                    "Curso de teste",
                    BigDecimal.valueOf(67),
                    CourseLevel.ADVANCED,
                    instructor.getId()
            );
            course.setId(1L);

            Module module = new Module(
                    "Modulo de Teste",
                    "Modulo de teste",
                    1L
            );
            module.setId(1L);

            Lesson lesson = new Lesson(
                    "Lição de teste",
                    "Lição de teste",
                    1L
            );
            lesson.setId(1L);

            module.addLesson(lesson);
            course.addModule(module);

            course.publish();

            assertEquals(CourseStatus.PUBLISHED, course.getStatus());
        }

        @Test
        @DisplayName("Deve lançar CourseWithoutModulesException")
        void shouldThrowCourseWithoutModulesException() {
            Course course = new Course(
                    "Curso de teste",
                    "Curso de teste",
                    BigDecimal.valueOf(67),
                    CourseLevel.ADVANCED,
                    instructor.getId()
            );
            course.setId(1L);

            assertThrows(CourseWithoutModulesException.class, course::publish);
        }

        @Test
        @DisplayName("Deve lançar CourseWithoutLessonsException")
        void shouldThrowCourseWithoutLessonException() {
            Course course = new Course(
                    "Curso de teste",
                    "Curso de teste",
                    BigDecimal.valueOf(67),
                    CourseLevel.ADVANCED,
                    instructor.getId()
            );
            course.setId(1L);

            Module module = new Module(
                    "Modulo de Teste",
                    "Modulo de teste",
                    1L
            );
            module.setId(1L);

            course.addModule(module);

            assertThrows(CourseWithoutLessonsException.class, course::publish);
        }
    }

    @Test
    @DisplayName("Arquivar curso")
    void archive() {
        Course course = new Course(
                "Curso de teste",
                "Teste",
                BigDecimal.valueOf(67),
                CourseLevel.ADVANCED,
                instructor.getId()
        );
        course.setId(1L);

        course.archive();

        assertEquals(CourseStatus.ARCHIVED, course.getStatus());
    }

    @Nested
    @DisplayName("Mudar ordem dos modulos")
    class changeModuleOrder {
        @Test
        @DisplayName("Mudar ordem dos módulos")
        void changeModuleOrderSuccessfully() {
            Course course = new Course(
                    "Curso de teste",
                    "Curso de teste",
                    BigDecimal.valueOf(67),
                    CourseLevel.ADVANCED,
                    instructor.getId()
            );
            course.setId(1L);

            Module module1 = new Module(
                    "Modulo de Teste 1",
                    "Modulo de teste 1",
                    1L
            );
            module1.setId(1L);

            Module module2 = new Module(
                    "Modulo de Teste 2",
                    "Modulo de teste 2",
                    2L
            );
            module2.setId(2L);

            Module module3 = new Module(
                    "Modulo de Teste 3",
                    "Modulo de teste 3",
                    3L
            );
            module3.setId(3L);

            course.addModule(module1);
            course.addModule(module2);
            course.addModule(module3);

            course.changeModuleOrder(2L, 0);
            assertEquals(List.of(module2, module1, module3), course.getModules());
            assertEquals(1L, module1.getOrderIndex());
            assertEquals(0L, module2.getOrderIndex());
            assertEquals(2L, module3.getOrderIndex());

            course.changeModuleOrder(2L, 3);
            assertEquals(List.of(module1, module3, module2), course.getModules());
            assertEquals(0L, module1.getOrderIndex());
            assertEquals(2L, module2.getOrderIndex());
            assertEquals(1L, module3.getOrderIndex());

            course.changeModuleOrder(3L, -1);
            assertEquals(List.of(module3, module1, module2), course.getModules());
            assertEquals(1L, module1.getOrderIndex());
            assertEquals(2L, module2.getOrderIndex());
            assertEquals(0L, module3.getOrderIndex());
        }

        @Test
        @DisplayName("Tenta mudar ordem dos módulos com id inválido")
        void changeModuleOrderWithModuleIdInvalid() {
            Course course = new Course(
                    "Curso de teste",
                    "Curso de teste",
                    BigDecimal.valueOf(67),
                    CourseLevel.ADVANCED,
                    instructor.getId()
            );
            course.setId(1L);

            Module module1 = new Module(
                    "Modulo de Teste 1",
                    "Modulo de teste 1",
                    1L
            );
            module1.setId(1L);

            Module module2 = new Module(
                    "Modulo de Teste 2",
                    "Modulo de teste 2",
                    2L
            );
            module2.setId(2L);

            course.addModule(module1);
            course.addModule(module2);

            assertThrows(ModuleOfOtherCourseException.class, () -> course.changeModuleOrder(3L, 0));
        }
    }

    @Nested
    @DisplayName("Verificar se curso está publicado")
    class isPublished {
        @Test
        @DisplayName("Deve retornar true quando o curso estiver publicado")
        void isPublishedReturnsTrue() {
            Course course = new Course(
                    "Curso de Teste",
                    "Teste",
                    BigDecimal.valueOf(67),
                    CourseLevel.BEGINNER,
                    instructor.getId()
            );
            course.setStatus(CourseStatus.PUBLISHED);

            assertTrue(course.isPublished());
        }

        @Test
        @DisplayName("Deve retornar false quando o curso não estiver publicado")
        void isPublishedReturnsFalse() {
            Course course = new Course(
                    "Curso de Teste",
                    "Teste",
                    BigDecimal.valueOf(67),
                    CourseLevel.BEGINNER,
                    instructor.getId()
            );

            assertFalse(course.isPublished());
        }
    }

}
