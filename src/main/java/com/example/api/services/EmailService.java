package com.example.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarEmailRecuperacao(String destinatario, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Recuperação de Senha");
        message.setText("Seu código de recuperação de senha é: " + token);
        mailSender.send(message);
    }

    public void enviarEmailComToken(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperação de Senha - Sistema de Finanças");
        message.setText("Seu código de recuperação de senha é: " + token);
        mailSender.send(message);
    }
}
