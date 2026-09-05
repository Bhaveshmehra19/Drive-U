package com.examly.springapp.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${mail.provider:smtp}") private String provider;
    @Value("${mail.api.key:}") private String apiKey;
    @Value("${mail.from.email:${spring.mail.username:}}") private String fromEmail;
    @Value("${mail.from.name:DriveU}") private String fromName;

    public EmailService(JavaMailSender mailSender) { this.mailSender = mailSender; }

    public void sendPlainText(String to, String subject, String text) {
        if ("brevo".equalsIgnoreCase(provider)) { sendViaBrevo(to, subject, text); return; }
        SimpleMailMessage message = new SimpleMailMessage();
        if (fromEmail != null && !fromEmail.isBlank()) message.setFrom(fromEmail);
        message.setTo(to); message.setSubject(subject); message.setText(text); mailSender.send(message);
    }

    private void sendViaBrevo(String to, String subject, String text) {
        if (apiKey == null || apiKey.isBlank() || fromEmail == null || fromEmail.isBlank())
            throw new IllegalStateException("Hosted email is not configured.");
        HttpHeaders headers = new HttpHeaders(); headers.setContentType(MediaType.APPLICATION_JSON); headers.set("api-key", apiKey);
        Map<String,Object> sender = new HashMap<>(); sender.put("email", fromEmail); sender.put("name", fromName);
        Map<String,Object> recipient = new HashMap<>(); recipient.put("email", to);
        Map<String,Object> body = new HashMap<>(); body.put("sender", sender); body.put("to", new Map[]{recipient}); body.put("subject", subject); body.put("textContent", text);
        restTemplate.postForEntity("https://api.brevo.com/v3/smtp/email", new HttpEntity<>(body, headers), String.class);
    }
}
