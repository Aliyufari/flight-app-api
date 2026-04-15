package com.afgicafe.flight.repository;

import com.afgicafe.flight.domain.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(
            @NotBlank(message = "Email is required")
            @Email(message = "Email must be valid")
            @Size(max = 150) String email);

    boolean existsByPhoneNumber(
            @NotBlank(message = "Phone number is required")
            @Size(min = 7, max = 20) @Pattern(regexp = "^[0-9+]+$",
                    message = "Invalid phone number") String phoneNumber);

    Optional<User> findByEmail(String email);
}
