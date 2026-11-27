package com.kawevk.vkurso.course.unit;

import com.kawevk.vkurso.course.*;
import com.kawevk.vkurso.course.dtos.CourseResponse;
import com.kawevk.vkurso.course.dtos.CreateCourseRequest;
import com.kawevk.vkurso.course.dtos.UpdateCourseRequest;
import com.kawevk.vkurso.course.exceptions.CourseRequestNotAllowed;
import com.kawevk.vkurso.course.exceptions.DuplicateSlugException;
import com.kawevk.vkurso.courseCategory.CourseCategoryRepository;
import com.kawevk.vkurso.enrollment.EnrollmentRepository;
import com.kawevk.vkurso.lesson.LessonRepository;
import com.kawevk.vkurso.shared.persistence.Modifiable;
import com.kawevk.vkurso.user.Role;
import com.kawevk.vkurso.user.User;
import com.kawevk.vkurso.user.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @InjectMocks
    private CourseService service;

    @Mock
    private CourseRepository repository;
    @Mock
    private CourseCategoryRepository courseCategoryRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private CourseMapper mapper;

    private User instructor;
    private User user;
    private CreateCourseRequest request;
    private UpdateCourseRequest updateRequest;
    private Course course;

    @Nested
    @DisplayName("Criar curso")
    class create {

        @BeforeEach
        void setUp() {
            instructor = mock(User.class);
            user = mock(User.class);
            course = mock(Course.class);

            request = new CreateCourseRequest(
                    "Curso de Teste: Básico ao Avançado",
                    "Curso de Teste",
                    CourseLevel.BEGINNER,
                    BigDecimal.valueOf(90)
            );

            updateRequest = new UpdateCourseRequest(
                    "Curso de Teste: SPRING BOOT",
                    "Curso de Teste",
                    CourseLevel.BEGINNER,
                    BigDecimal.valueOf(90)
            );
        }

        @Test
        @DisplayName("Criar curso com sucesso")
        void shouldCreateCourseSuccessfully() {
            when(instructor.getId()).thenReturn(1L);
            when(instructor.getRole()).thenReturn(Role.INSTRUCTOR);

            CourseResponse expectedResponse = mock(CourseResponse.class);

            when(repository.existsBySlug(anyString()))
                    .thenReturn(false);

            when(repository.save(any(Course.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            when(userRepository.findById(1L)).thenReturn(Optional.of(instructor));

            when(courseCategoryRepository.findAllById(anyIterable()))
                    .thenReturn(List.of());

            when(lessonRepository.countByCourseId(any()))
                    .thenReturn(0L);

            when(enrollmentRepository.countByCourseId(any()))
                    .thenReturn(0L);

            when(mapper.toResponse(
                    any(Course.class),
                    any(User.class),
                    anyList(),
                    anyLong(),
                    anyLong()
            )).thenReturn(expectedResponse);

            CourseResponse response = service.create(request, instructor);

            assertThat(response).isSameAs(expectedResponse);

            ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);

            verify(repository).save(courseCaptor.capture());

            Course course = courseCaptor.getValue();

            verify(repository).existsBySlug("curso-de-teste-basico-ao-avancado");

            assertAll(
                    () -> assertEquals("Curso de Teste: Básico ao Avançado", course.getTitle()),
                    () -> assertEquals("Curso de Teste", course.getDescription()),
                    () -> assertEquals(BigDecimal.valueOf(90), course.getPrice()),
                    () -> assertEquals(CourseLevel.BEGINNER, course.getLevel()),
                    () -> assertEquals(1L, course.getInstructorId())
            );

            assertNotNull(response);
        }

        @Test
        @DisplayName("Criar curso com slug duplicado")
        void shouldThrowDuplicateSlugException() {
            when(repository.existsBySlug("curso-de-teste-basico-ao-avancado"))
                    .thenReturn(true);

            when(instructor.getRole()).thenReturn(Role.INSTRUCTOR);

            assertThrows(DuplicateSlugException.class, () -> service.create(request, instructor));
            verify(repository).existsBySlug("curso-de-teste-basico-ao-avancado");
        }

        @Test
        @DisplayName("Aluno cria curso")
        void shouldThrowCourseRequestNotAllowedException() {
            when(user.getRole()).thenReturn(Role.USER);
            assertThrows(CourseRequestNotAllowed.class, () -> service.create(request, user));
        }
    }

    @Nested
    @DisplayName("Atualizar cursos")
    class update {

        @BeforeEach
        void setUp() {
            instructor = mock(User.class);
            user = mock(User.class);

            course = new Course(
                    "Curso de Teste",
                    "Descrição original",
                    BigDecimal.valueOf(60),
                    CourseLevel.BEGINNER,
                    1L
            );
            course.setId(1L);

            updateRequest = new UpdateCourseRequest(
                    "Curso de Teste: Atualização",
                    "Descrição atualizada",
                    CourseLevel.INTERMEDIATE,
                    BigDecimal.valueOf(90)
            );
        }

        @Test
        @DisplayName("Atualizar curso com sucesso")
        void shouldUpdateCourse() {
            when(repository.findById(1L)).thenReturn(Optional.of(course));
            when(instructor.getId()).thenReturn(1L);
            when(instructor.getRole()).thenReturn(Role.INSTRUCTOR);

            when(userRepository.findById(1L))
                    .thenReturn(Optional.of(instructor));

            when(repository.existsBySlug(anyString()))
                    .thenReturn(false);

            when(courseCategoryRepository.findAllById(anyIterable()))
                    .thenReturn(List.of());

            when(lessonRepository.countByCourseId(anyLong()))
                    .thenReturn(0L);

            when(enrollmentRepository.countByCourseId(anyLong()))
                    .thenReturn(0L);

            CourseResponse expectedResponse = mock(CourseResponse.class);

            when(mapper.toResponse(
                    any(Course.class),
                    any(User.class),
                    anyList(),
                    anyLong(),
                    anyLong()
            )).thenReturn(expectedResponse);

            CourseResponse response = service.update(1L, updateRequest, instructor);

            assertThat(response).isSameAs(expectedResponse);

            verify(repository).findById(1L);
            verify(repository).existsBySlug("curso-de-teste-atualizacao");

            assertAll(
                    () -> assertEquals("Curso de Teste: Atualização", course.getTitle()),
                    () -> assertEquals("curso-de-teste-atualizacao", course.getSlug()),
                    () -> assertEquals("Descrição atualizada", course.getDescription()),
                    () -> assertEquals(CourseLevel.INTERMEDIATE, course.getLevel()),
                    () -> assertEquals(BigDecimal.valueOf(90), course.getPrice())
            );
        }
    }
}