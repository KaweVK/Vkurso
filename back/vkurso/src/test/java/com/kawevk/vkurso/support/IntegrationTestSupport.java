package com.kawevk.vkurso.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kawevk.vkurso.shared.storage.VideoStorageService;
import com.kawevk.vkurso.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Classe base dos testes de integração. O que ela resolve, uma vez só, pra todo mundo que estende:
 *
 *  - Sobe o contexto Spring de verdade (@SpringBootTest) com MockMvc, sem precisar de porta HTTP real.
 *  - Ativa o profile "test" (application-test.yml): banco vkurso_test + sessão sem Redis.
 *  - Mocka o VideoStorageService (MinIO): os testes daqui validam REGRA DE NEGÓCIO
 *    (ownership, matrícula, o que fica salvo no banco) — não o SDK do MinIO em si.
 *  - Envolve cada teste numa transação sempre revertida no final (@Transactional): um teste
 *    nunca "suja" o banco pro próximo, sem precisar de limpeza manual.
 *  - Fornece métodos utilitários pra criar usuários, logar, criar cursos/módulos/aulas e publicar cursos.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class IntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    protected VideoStorageService videoStorageService;

    private static final String SENHA_PADRAO = "vkurso1234";

    @BeforeEach
    void configurarStorageFake() {
        when(videoStorageService.upload(any(), any(), any())).thenReturn("fake-key");
        when(videoStorageService.presignedGetUrl(any())).thenReturn("https://fake-minio.local/fake-key");
    }

    // ---------------------------------------------------------------
    // Usuários e login
    // ---------------------------------------------------------------

    /** Cria um usuário via POST /api/users (endpoint público) e devolve o id gerado. */
    protected Long createUser(String fullName, String email, String password, Role role) throws Exception {
        String body = """
                {"fullName":"%s","email":"%s","password":"%s","role":"%s"}
                """.formatted(fullName, email, password, role);

        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    protected MockHttpSession login(String email, String password) throws Exception {
        String body = """
                {"email":"%s","password":"%s"}
                """.formatted(email, password);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();

        return (MockHttpSession) result.getRequest().getSession(false);
    }

    /** Cria um instrutor e já devolve a sessão logada dele. */
    protected MockHttpSession createAndLoginInstructor(String prefixoUnico) throws Exception {
        String email = prefixoUnico + "@vkurso.test";
        createUser("Instrutor " + prefixoUnico, email, SENHA_PADRAO, Role.INSTRUCTOR);
        return login(email, SENHA_PADRAO);
    }

    /** Cria um aluno (Role.USER) e já devolve a sessão logada dele. */
    protected MockHttpSession createAndLoginStudent(String prefixoUnico) throws Exception {
        String email = prefixoUnico + "@vkurso.test";
        createUser("Aluno " + prefixoUnico, email, SENHA_PADRAO, Role.USER);
        return login(email, SENHA_PADRAO);
    }

    // ---------------------------------------------------------------
    // Curso / módulo / aula
    // ---------------------------------------------------------------

    /** Cria um curso e já devolve o id dele. */
    protected Long createCourse(MockHttpSession session, String title) throws Exception {
        String body = """
                {"title":"%s","description":"descrição de teste","level":"BEGINNER","price":99.90}
                """.formatted(title);

        MvcResult result = mockMvc.perform(post("/api/courses")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    protected Long createModule(MockHttpSession session, Long courseId, String title) throws Exception {
        String body = """
                {"title":"%s","description":"descrição de teste","orderIndex":0}
                """.formatted(title);

        MvcResult result = mockMvc.perform(post("/api/courses/{courseId}/modules", courseId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    protected Long createLesson(MockHttpSession session, Long courseId, Long moduleId, String title) throws Exception {
        String body = """
                {"title":"%s","description":"descrição de teste","orderIndex":0}
                """.formatted(title);

        MvcResult result = mockMvc.perform(post("/api/courses/{courseId}/modules/{moduleId}/lessons", courseId, moduleId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    protected MvcResult publishCourseRaw(MockHttpSession session, Long courseId) throws Exception {
        return mockMvc.perform(post("/api/courses/{id}/publish", courseId).session(session)).andReturn();
    }

    /** IDs de um curso já criado, com 1 módulo e 1 aula. */
    protected record CourseFixture(Long courseId, Long moduleId, Long lessonId) {}

    /** Monta curso -> módulo -> aula e publica. Combinação usada por vários testes (matrícula, vídeo). */
    protected CourseFixture createPublishedCourseWithLesson(MockHttpSession instructorSession, String titlePrefix) throws Exception {
        Long courseId = createCourse(instructorSession, titlePrefix + " - Curso");
        Long moduleId = createModule(instructorSession, courseId, titlePrefix + " - Módulo");
        Long lessonId = createLesson(instructorSession, courseId, moduleId, titlePrefix + " - Aula");
        publishCourseRaw(instructorSession, courseId);
        return new CourseFixture(courseId, moduleId, lessonId);
    }
}