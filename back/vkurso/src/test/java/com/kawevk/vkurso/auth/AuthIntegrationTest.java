package com.kawevk.vkurso.auth;

import com.kawevk.vkurso.support.IntegrationTestSupport;
import com.kawevk.vkurso.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        createUser("Vkurso user", "teste@vkurso.com","vkursos1234", Role.USER);
    }

    @Test
    void loginComCredencialValida_CriaSessaoAutenticada() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "teste@vkurso.com","password": "vkursos1234"}
                                """))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(204);

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        assertThat(session).isNotNull();
        assertThat(session.getAttribute("SPRING_SECURITY_CONTEXT")).isNotNull();
    }

    @Test
    void loginComSenhaErrada() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "teste@vkurso.com","password": "vkursos12345"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginComEmailInexistente() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "testes@vkurso.com","password": "vkursos1234"}
                                """))
                .andExpect(status().isUnauthorized());
    }
}