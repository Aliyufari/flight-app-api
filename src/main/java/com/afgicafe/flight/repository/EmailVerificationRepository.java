package com.afgicafe.flight.repository;

import com.afgicafe.flight.domain.entity.EmailVerification;
import com.afgicafe.flight.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {
    Optional<EmailVerification> findByToken(String token);

    Optional<EmailVerification> findByUser(User user);
}
