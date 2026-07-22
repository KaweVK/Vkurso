package com.kawevk.vkurso.lesson;

import com.kawevk.vkurso.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LessonVideoAccessIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private LessonRepository lessonRepository;

    private CourseFixture cursoComVideo(MockHttpSession instrutor, String prefixo) throws Exception {
        CourseFixture curso = createPublishedCourseWithLesson(instrutor, prefixo);

        MockMultipartFile file = new MockMultipartFile("file", "aula.mp4", "video/mp4", "conteudo-fake".getBytes());
        mockMvc.perform(multipart("/api/courses/{courseId}/modules/{moduleId}/lessons/{id}/video",
                curso.courseId(), curso.moduleId(), curso.lessonId())
                .file(file)
                .session(instrutor));

        return curso;
    }

    @Test
    void alunoMatriculadoPedeUrlDoVideo_RecebePresignedUrl() throws Exception {
        MockHttpSession instrutor = createAndLoginInstructor("prof1");
        CourseFixture curso = cursoComVideo(instrutor, "Curso Acesso 1");

        MockHttpSession aluno = createAndLoginStudent("aluno1");
        mockMvc.perform(post("/api/courses/{courseId}/enrollments", curso.courseId()).session(aluno))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/courses/{courseId}/modules/{moduleId}/lessons/{id}/video-url",
                        curso.courseId(), curso.moduleId(), curso.lessonId())
                        .session(aluno))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.videoUrl").value("https://fake-minio.local/fake-key"));
    }

    @Test
    void alunoNaoMatriculadoComFreePreview_RecebePresignedUrl() throws Exception {
        MockHttpSession instrutor = createAndLoginInstructor("prof2");
        CourseFixture curso = cursoComVideo(instrutor, "Curso Acesso 2");

        Lesson lesson = lessonRepository.findById(curso.lessonId()).orElseThrow();
        lesson.setFreePreview(true);
        lessonRepository.save(lesson);

        MockHttpSession aluno = createAndLoginStudent("aluno2");

        mockMvc.perform(get("/api/courses/{courseId}/modules/{moduleId}/lessons/{id}/video-url",
                        curso.courseId(), curso.moduleId(), curso.lessonId())
                        .session(aluno))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.videoUrl").value("https://fake-minio.local/fake-key"));
    }

    @Test
    void alunoNaoMatriculadoSemFreePreview_AcessoNegado() throws Exception {
        MockHttpSession instrutor = createAndLoginInstructor("prof3");
        CourseFixture curso = cursoComVideo(instrutor, "Curso Acesso 3");

        MockHttpSession aluno = createAndLoginStudent("aluno3");

        mockMvc.perform(get("/api/courses/{courseId}/modules/{moduleId}/lessons/{id}/video-url",
                        curso.courseId(), curso.moduleId(), curso.lessonId())
                        .session(aluno))
                .andExpect(status().isForbidden());
    }
}