package com.afgicafe.flight.unit;

import com.afgicafe.flight.domain.entity.EmailVerification;
import com.afgicafe.flight.domain.entity.User;
import com.afgicafe.flight.repository.EmailVerificationRepository;
import com.afgicafe.flight.repository.UserRepository;
import com.afgicafe.flight.service.impl.EmailVerificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
        import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailVerificationServiceTest {

    @Mock
    private EmailVerificationRepository verificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmailVerificationServiceImpl service;

    @Test
    void shouldVerifyEmailSuccessfully() {

        String token = "valid-token";

        User user = new User();
        user.setEmail("johndoe@email.com");

        EmailVerification verification = new EmailVerification(user);
        verification.setToken(token);

        when(verificationRepository.findByToken(token))
                .thenReturn(Optional.of(verification));

        service.verifyToken(token);

        assertTrue(verification.isVerified());
        verify(userRepository, times(1)).save(user);
        verify(verificationRepository, times(1)).save(verification);
    }
}