package com.kawevk.vkurso.enrollment;

import com.kawevk.vkurso.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EnrollmentIntegrationTest extends IntegrationTestSupport {

    @Test
    void alunoSeMatriculaEmCursoPublicado_FicaAtiva() throws Exception {
        MockHttpSession instrutor = createAndLoginInstructor("prof1");
        CourseFixture curso = createPublishedCourseWithLesson(instrutor, "Java Básico");

        MockHttpSession aluno = createAndLoginStudent("aluno1");

        mockMvc.perform(post("/api/courses/{courseId}/enrollments", curso.courseId()).session(aluno))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void alunoCancelaEMatriculaDeNovo_ReativaMesmaMatricula() throws Exception {
        MockHttpSession instrutor = createAndLoginInstructor("prof2");
        CourseFixture curso = createPublishedCourseWithLesson(instrutor, "Java Intermediário");
        MockHttpSession aluno = createAndLoginStudent("aluno2");

        MvcResult primeiraMatricula = mockMvc.perform(post("/api/courses/{courseId}/enrollments", curso.courseId()).session(aluno))
                .andExpect(status().isCreated())
                .andReturn();
        long idOriginal = objectMapper.readTree(primeiraMatricula.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(delete("/api/courses/{courseId}/enrollments", curso.courseId()).session(aluno))
                .andExpect(status().isNoContent());

        MvcResult segundaMatricula = mockMvc.perform(post("/api/courses/{courseId}/enrollments", curso.courseId()).session(aluno))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andReturn();
        long idReativado = objectMapper.readTree(segundaMatricula.getResponse().getContentAsString()).get("id").asLong();

        assertThat(idReativado).isEqualTo(idOriginal);
    }

    @Test
    void alunoTentaSeMatricularEmCursoNaoPublicado() throws Exception {
        MockHttpSession instrutor = createAndLoginInstructor("prof3");
        Long courseId = createCourse(instrutor, "Curso Ainda Em Rascunho");
        createModule(instrutor, courseId, "Módulo 1");

        MockHttpSession aluno = createAndLoginStudent("aluno3");

        mockMvc.perform(post("/api/courses/{courseId}/enrollments", courseId).session(aluno))
                .andExpect(status().isUnprocessableContent());
    }
}