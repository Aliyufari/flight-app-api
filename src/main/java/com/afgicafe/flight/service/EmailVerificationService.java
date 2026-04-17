package com.afgicafe.flight.service;

import com.afgicafe.flight.domain.entity.User;
import com.afgicafe.flight.dto.request.ResendVerificationRequest;

public interface EmailVerificationService {
    void createVerification(User user);
    void verifyToken(String token);
    void resendVerification(ResendVerificationRequest request);
}
