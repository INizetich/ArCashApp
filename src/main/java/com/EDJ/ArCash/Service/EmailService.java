package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.Models.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void testEmail(User user, String token) throws MessagingException, UnsupportedEncodingException {
        Context context = new Context();
        context.setVariable("username", user.getName());
        context.setVariable("token", token);

        String html = templateEngine.process("email", context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

        // Establecer el nombre del remitente y la dirección de correo
        helper.setFrom("helparcash2025@gmail.com", "ArCash");

        helper.setTo(user.getEmail());
        helper.setSubject("¡Bienvenido a ArCashApp, " + user.getName() + "!");

        // Asegurar que el contenido sea HTML y usar UTF-8 para evitar problemas de codificación
        helper.setText(html, true);

        mailSender.send(mimeMessage);
    }
}
