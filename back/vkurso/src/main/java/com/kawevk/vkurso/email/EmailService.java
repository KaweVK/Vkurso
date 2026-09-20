package com.kawevk.vkurso.email;

import com.resend.core.exception.ResendException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import com.resend.*;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Service
public class EmailService {

    private final Resend resend;
    @Value("${MAIL_USERNAME}")
    private String emailFrom;

    public EmailService(@Value("${RESEND_API_KEY}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    public void sendVerificationCode(String email, String code) {
        try {
            String html = loadTemplate().replace("{{CODE}}", code);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(emailFrom)
                    .to(email)
                    .subject("Verificação de e-mail - Vkursos")
                    .html(html)
                    .build();

            CreateEmailResponse data = resend.emails().send(params);
        } catch (ResendException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String loadTemplate() throws IOException {
        ClassPathResource resource =
                new ClassPathResource("templates/email/verification-code.html");

        return resource.getContentAsString(StandardCharsets.UTF_8);
    }
}
