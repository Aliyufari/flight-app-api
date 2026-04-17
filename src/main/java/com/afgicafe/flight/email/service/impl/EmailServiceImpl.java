package com.afgicafe.flight.email.service.impl;

import com.afgicafe.flight.email.service.EmailService;
import com.afgicafe.flight.utils.EmailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmailVerificationMessage(String name, String to, String token) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Account Verification");
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(EmailUtils.getEmailVerificationMessage(name, host, token));
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
