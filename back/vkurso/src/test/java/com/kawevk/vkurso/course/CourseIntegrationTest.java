package com.kawevk.vkurso.course;

import com.kawevk.vkurso.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CourseIntegrationTest extends IntegrationTestSupport {

    @Test
    void instrutorCriaCursoComModuloEAula_EstruturaPersistidaEPublicavel() throws Exception {
        MockHttpSession instrutor = createAndLoginInstructor("vkurso");
        Long courseId = createCourse(instrutor, "Curso Completo");
        Long moduleId = createModule(instrutor, courseId, "Módulo 1");
        createLesson(instrutor, courseId, moduleId, "Aula 1");

        mockMvc.perform(get("/api/courses/id/{id}", courseId).session(instrutor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modules", hasSize(1)))
                .andExpect(jsonPath("$.modules[0].lessons", hasSize(1)));

        mockMvc.perform(post("/api/courses/{id}/publish", courseId).session(instrutor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PUBLISHED"));
    }

    @Test
    void instrutorCriaCursoComModuloSemAula_EstruturaPersistidaMasNaoPublica() throws Exception {
        MockHttpSession instrutor = createAndLoginInstructor("prof-cc2");

        Long courseId = createCourse(instrutor, "Curso Módulo Vazio");
        createModule(instrutor, courseId, "Módulo Sem Aula");

        mockMvc.perform(get("/api/courses/id/{id}", courseId).session(instrutor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modules", hasSize(1)))
                .andExpect(jsonPath("$.modules[0].lessons", hasSize(0)));

        mockMvc.perform(post("/api/courses/{id}/publish", courseId).session(instrutor))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    void instrutorCriaCursoSemModuloSemAula_EstruturaPersistidaMasNaoPublica() throws Exception {
        MockHttpSession instrutor = createAndLoginInstructor("prof-cc3");

        Long courseId = createCourse(instrutor, "Curso Vazio");

        mockMvc.perform(get("/api/courses/id/{id}", courseId).session(instrutor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modules", hasSize(0)));

        mockMvc.perform(post("/api/courses/{id}/publish", courseId).session(instrutor))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    void alunoTentaCriarCurso() throws Exception {
        MockHttpSession aluno = createAndLoginStudent("aluno");

        mockMvc.perform(post("/api/courses")
                        .session(aluno)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Curso do Aluno","description":"desc","level":"BEGINNER","price":10.00}
                                """))
                .andExpect(status().isForbidden());
    }
}