package com.example.SpringMailTest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.charset.StandardCharsets;

@RestController
public class EmailController {

    @Autowired
    private final JavaMailSender javaMailSender;

    public EmailController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @RequestMapping("/send-email")
    public String sendEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("lutencjusz@gmail.com");
            message.setTo("lutencjusz.test@gmail.com");
            message.setSubject("Testowy email ze Spring Boota");
            message.setText("Pozdrowienia z testu Spring Boota");
            javaMailSender.send(message);
            return "Zwykły email wysłany";
        } catch (Exception e) {
            return "Błąd podczas wysyłania email: " + e;
        }
    }

    @RequestMapping("/send-email-attachment")
    public String sendEmailWithAttachment() {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("lutencjusz@gmail.com");
            helper.setTo("lutencjusz.test@gmail.com");
            helper.setSubject("Testowy email z załącznikiem");
            helper.setText("Pozdrowienia z testu Spring Boota z załącznikiem");
            helper.addAttachment("logo.png", new File("C:\\Users\\micha\\Dropbox\\Photos\\Portrety - Michal\\IMG_5595.JPG"));
            helper.addAttachment("logo.png", new File("C:\\Users\\micha\\Dropbox\\Photos\\Portrety - Michal\\IMG_5640.JPG"));
            javaMailSender.send(message);
            return "Mejl z załącznikiem wysłany";
        } catch (MessagingException e) {
            return "Błąd podczas wysyłania mejla z załącznikiem: " + e;
        }
    }

    @RequestMapping("/send-email-html-body")
    public String sendEmailWithHtmlBody() {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("lutencjusz@gmail.com");
            helper.setTo("lutencjusz.test@gmail.com");
            helper.setSubject("Testowy email z html body ze Spring Boota");
            try (var inputStream = EmailController.class.getResourceAsStream("/templates/email-template.html")) {
                assert inputStream != null;
                helper.setText(
                        new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), true
                );
            }
            helper.addInline("logo.png", new File("C:\\Users\\micha\\Dropbox\\Photos\\Portrety - Michal\\IMG_5595.JPG"));
            javaMailSender.send(message);
            return "Email z html body wysłany";
        } catch (Exception e) {
            return "Błąd podczas wysyłania mejla z html body: " + e;
        }
    }
}
