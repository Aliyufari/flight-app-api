package com.afgicafe.flight.service;

import com.afgicafe.flight.domain.entity.User;

public interface EmailVerificationService {
    void createVerification(User user);
    void verifyToken(String token);
    void resendVerification(String email);
}
