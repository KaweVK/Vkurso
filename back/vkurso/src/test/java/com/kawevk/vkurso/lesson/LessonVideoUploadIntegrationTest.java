package com.kawevk.vkurso.lesson;

import com.kawevk.vkurso.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LessonVideoUploadIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private LessonRepository lessonRepository;

    @Test
    void instrutorDonoFazUploadDeVideo_SucessoEVideoKeySalvo() throws Exception {
        MockHttpSession instrutor = createAndLoginInstructor("prof-up1");
        Long courseId = createCourse(instrutor, "Curso Upload 1");
        Long moduleId = createModule(instrutor, courseId, "Módulo 1");
        Long lessonId = createLesson(instrutor, courseId, moduleId, "Aula 1");

        MockMultipartFile file = new MockMultipartFile("file", "aula.mp4", "video/mp4", "conteudo-fake".getBytes());

        mockMvc.perform(multipart("/api/courses/{courseId}/modules/{moduleId}/lessons/{id}/video",
                        courseId, moduleId, lessonId)
                        .file(file)
                        .session(instrutor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.videoUrl").value("https://fake-minio.local/fake-key"));

        verify(videoStorageService).upload(eq(courseId), eq(lessonId), any());
        assertThat(lessonRepository.findById(lessonId).orElseThrow().getVideoKey()).isEqualTo("fake-key");
    }

    @Test
    void instrutorNaoDonoFazUpload_Retorna403PorOwnership() throws Exception {
        MockHttpSession dono = createAndLoginInstructor("prof-up2-dono");
        Long courseId = createCourse(dono, "Curso Upload 2");
        Long moduleId = createModule(dono, courseId, "Módulo 1");
        Long lessonId = createLesson(dono, courseId, moduleId, "Aula 1");

        MockHttpSession outroInstrutor = createAndLoginInstructor("prof-up2-outro");

        MockMultipartFile file = new MockMultipartFile("file", "aula.mp4", "video/mp4", "conteudo-fake".getBytes());

        mockMvc.perform(multipart("/api/courses/{courseId}/modules/{moduleId}/lessons/{id}/video",
                        courseId, moduleId, lessonId)
                        .file(file)
                        .session(outroInstrutor))
                .andExpect(status().isForbidden());

        verify(videoStorageService, never()).upload(any(), any(), any());
    }

    @Test
    void alunoTentaFazerUpload_Retorna403DoFiltroDeSeguranca() throws Exception {
        MockHttpSession instrutor = createAndLoginInstructor("prof-up3");
        Long courseId = createCourse(instrutor, "Curso Upload 3");
        Long moduleId = createModule(instrutor, courseId, "Módulo 1");
        Long lessonId = createLesson(instrutor, courseId, moduleId, "Aula 1");

        MockHttpSession aluno = createAndLoginStudent("aluno-up3");

        MockMultipartFile file = new MockMultipartFile("file", "aula.mp4", "video/mp4", "conteudo-fake".getBytes());

        mockMvc.perform(multipart("/api/courses/{courseId}/modules/{moduleId}/lessons/{id}/video",
                        courseId, moduleId, lessonId)
                        .file(file)
                        .session(aluno))
                .andExpect(status().isForbidden());

        verify(videoStorageService, never()).upload(any(), any(), any());
    }

    @Test
    void instrutorTentaEditarAulaDeOutroInstrutor_Retorna403PorOwnership() throws Exception {
        MockHttpSession dono = createAndLoginInstructor("prof-up4-dono");
        Long courseId = createCourse(dono, "Curso Upload 4");
        Long moduleId = createModule(dono, courseId, "Módulo 1");
        Long lessonId = createLesson(dono, courseId, moduleId, "Aula Original");

        MockHttpSession outroInstrutor = createAndLoginInstructor("prof-up4-outro");

        mockMvc.perform(put("/api/courses/{courseId}/modules/{moduleId}/lessons/{id}",
                        courseId, moduleId, lessonId)
                        .session(outroInstrutor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Título Alterado","description":"desc","orderIndex":0}
                                """))
                .andExpect(status().isForbidden());
    }
}