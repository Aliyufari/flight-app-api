package com.afgicafe.flight.email.service;

public interface EmailService {
    void sendEmailVerificationMessage(String name, String to, String token);
}
