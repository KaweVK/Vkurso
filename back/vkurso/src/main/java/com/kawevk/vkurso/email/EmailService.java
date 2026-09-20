package com.kawevk.vkurso.email;

import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String email, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            helper.setTo(email);
            helper.setSubject("Verificação de e-mail - Vkursos");

            String html = loadTemplate().replace("{{CODE}}", code);

            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        } catch (jakarta.mail.MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String loadTemplate() throws IOException {
        ClassPathResource resource =
                new ClassPathResource("templates/email/verification-code.html");

        return resource.getContentAsString(StandardCharsets.UTF_8);
    }
}
