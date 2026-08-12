package com.kawevk.vkurso.course;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.kawevk.vkurso.course.dtos.CourseResponse;
import com.kawevk.vkurso.course.dtos.CreateCourseRequest;
import com.kawevk.vkurso.course.exceptions.CourseNotFoundException;
import com.kawevk.vkurso.course.exceptions.CourseRequestNotAllowed;
import com.kawevk.vkurso.course.exceptions.DuplicateSlugException;
import com.kawevk.vkurso.user.Role;
import com.kawevk.vkurso.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository repository;

    @InjectMocks
    private CourseService courseService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User instrutor;
    private User aluno;
    private User admin;
    private Course curso;

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode(anyString()))
                .thenReturn("senha-criptografada");

        instrutor = new User(
                "Instrutor",
                "instrutor@gmail.com",
                passwordEncoder.encode("senha"),
                Role.INSTRUCTOR);
        instrutor.setId(1L);
        instrutor.setRole(Role.INSTRUCTOR);

        aluno = new User(
                "Aluno",
                "aluno@gmail.com",
                passwordEncoder.encode("senha"),
                Role.USER);
        aluno.setId(2L);
        aluno.setRole(Role.USER);

        admin = new User(
                "Admin",
                "admin@gmail.com",
                passwordEncoder.encode("senha"),
                Role.ADMIN
        );
        admin.setId(99L);
        admin.setRole(Role.ADMIN);

        curso = new Course(
                "Java Spring",
                "Backend Course",
                BigDecimal.TEN,
                CourseLevel.BEGINNER,
                instrutor.getId());
        curso.setId(100L);
    }

    // =========================
    // ---- TESTES DE BUSCA ----
    // =========================

    // -- Listagem --

    @Test
    void list_DeveRetornarLista() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> page = new PageImpl<>(
                List.of(curso),
                pageable,
                1
        );

        when(repository.findAll(pageable)).thenReturn(page);

        Page<CourseResponse> responsePage = courseService.list(pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(1, responsePage.getContent().size());

        verify(repository).findAll(pageable);
    }

    @Test
    void list_DeveRetornarPaginaVaziaQuandoNaoHouverCursos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> page = new PageImpl<>(
                List.of(),
                pageable,
                0
        );

        when(repository.findAll(pageable)).thenReturn(page);

        Page<CourseResponse> response = courseService.list(pageable);

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(repository).findAll(pageable);
    }

    @Test
    void ListByInstructor_DeveRetornarListaDeCursosPublicadosPeloInstrutorInformado() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> page = new PageImpl<>(
                List.of(curso),
                pageable,
                1
        );

        when(repository.findAllByInstructorId(instrutor.getId(), pageable)).thenReturn(page);

        Page<CourseResponse> response =
                courseService.listByInstructor(
                        instrutor.getId(),
                        pageable
                );

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(100L, response.getContent().getFirst().id());

        verify(repository).findAllByInstructorId(instrutor.getId(), pageable);
    }

    @Test
    void ListByInstructor_DeveRetornarListaVaziaQuandoNaoHouverCursosPublicadosPeloInstrutorInformado() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> page = new PageImpl<>(
                List.of(),
                pageable,
                0
        );

        when(repository.findAllByInstructorId(instrutor.getId(), pageable)).thenReturn(page);

        Page<CourseResponse> response =
                courseService.listByInstructor(
                        instrutor.getId(),
                        pageable
                );

        assertNotNull(response);
        assertEquals(0, response.getTotalElements());
        assertTrue(response.isEmpty());

        verify(repository).findAllByInstructorId(instrutor.getId(), pageable);
    }

    // -- gets

    @Test
    void getById_DeveRetornarCursoComSucesso() {
        when(repository.findById(100L)).thenReturn(Optional.of(curso));

        CourseResponse response = courseService.get(100L);

        assertNotNull(response);
        verify(repository).findById(100L);
    }

    @Test
    void getById_DeveLancarExcecaoQuandoCursoNaoExistir() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.get(999L));

        verify(repository).findById(999L);
    }

    @Test
    void getBySlug_DeveRetornarCursoComSucesso() {
        when(repository.findBySlug("java-spring")).thenReturn(Optional.of(curso));

        CourseResponse response = courseService.getBySlug("java-spring");

        assertNotNull(response);
        assertEquals(curso.getId(), response.id());
        verify(repository).findBySlug("java-spring");
    }

    @Test
    void getBySlug_DeveLancarExcecaoQuandoCursoNaoExistir() {
        when(repository.findBySlug("java-spring")).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.getBySlug("java-spring"));

        verify(repository).findBySlug("java-spring");
    }

    // =========================
    // --- TESTES DE CRIAÇÃO ---
    // =========================

    @Test
    void create_DeveSalvarERetornarCursoComoDraftSlugGeradoEInstrutorAssociado() {
        CreateCourseRequest request = new CreateCourseRequest(
                "Novo Curso",
                "Desc",
                CourseLevel.BEGINNER,
                BigDecimal.ONE
        );

        when(repository.existsBySlug(anyString())).thenReturn(false);
        when(repository.save(any(Course.class))).thenReturn(curso);

        CourseResponse response = courseService.create(request, instrutor);

        assertNotNull(response);
        assertEquals("java-spring", response.slug());
        assertEquals(instrutor.getId(), response.instructorId());
        assertEquals(CourseStatus.DRAFT, response.status());

        verify(repository).save(any(Course.class));
    }

    @Test
    void create_DeveRetornarExcecaoQuandoUserNaoForInstrutor() {
        CreateCourseRequest request = new CreateCourseRequest(
                "Novo Curso",
                "Desc",
                CourseLevel.BEGINNER,
                BigDecimal.ONE
        );

        assertThrows(CourseRequestNotAllowed.class, () -> courseService.create(request, aluno));
    }

    @Test
    void create_DeveRetornarExcecaoSeSlugJaExistir() {
        CreateCourseRequest request = new CreateCourseRequest(
                "Novo Curso",
                "Desc",
                CourseLevel.BEGINNER,
                BigDecimal.ONE
        );

        when(repository.existsBySlug("novo-curso")).thenReturn(true);

        assertThrows(DuplicateSlugException.class, () -> courseService.create(request, instrutor));

        verify(repository).existsBySlug("novo-curso");
    }



}