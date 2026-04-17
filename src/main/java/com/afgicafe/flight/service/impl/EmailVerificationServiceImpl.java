package com.afgicafe.flight.service.impl;

import com.afgicafe.flight.domain.entity.EmailVerification;
import com.afgicafe.flight.domain.entity.User;
import com.afgicafe.flight.domain.enums.Status;
import com.afgicafe.flight.domain.event.EmailVerificationEvent;
import com.afgicafe.flight.dto.request.ResendVerificationRequest;
import com.afgicafe.flight.event.publisher.EmailEventPublisher;
import com.afgicafe.flight.exception.BadRequestException;
import com.afgicafe.flight.exception.ResourceNotFoundException;
import com.afgicafe.flight.repository.EmailVerificationRepository;
import com.afgicafe.flight.repository.UserRepository;
import com.afgicafe.flight.service.EmailVerificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private final EmailVerificationRepository repository;
    private final UserRepository userRepository;
    private final EmailEventPublisher emailEventPublisher;

    @Override
    public void createVerification(User user) {
        EmailVerification verification = new EmailVerification(user);
        repository.save(verification);

        emailEventPublisher.publish(
                new EmailVerificationEvent(
                        user.getFirstName() + " " + user.getLastName(),
                        user.getEmail(),
                        verification.getToken()
                )
        );
    }

    @Override
    public void verifyToken(String token) {

        EmailVerification verification = repository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid verification token"));

        if (verification.isVerified()) {
            throw new BadRequestException("Email already verified");
        }

        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Verification token has expired");
        }

        User user = verification.getUser();

        user.setStatus(Status.ACTIVE);
        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        verification.setVerified(true);
        repository.save(verification);
    }

    @Override
    @Transactional
    public void resendVerification(ResendVerificationRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getStatus() == Status.ACTIVE) {
            throw new BadRequestException("Account already verified");
        }

        EmailVerification verification = new EmailVerification(user);
        repository.save(verification);

        emailEventPublisher.publish(
                new EmailVerificationEvent(
                        user.getFirstName() + " " + user.getLastName(),
                        user.getEmail(),
                        verification.getToken()
                )
        );
    }
}
