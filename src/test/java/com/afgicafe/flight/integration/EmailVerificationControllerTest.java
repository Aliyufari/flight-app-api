package com.afgicafe.flight.integration;

import com.afgicafe.flight.config.TestSecurityConfig;
import com.afgicafe.flight.config.seeder.UserSeeder;
import com.afgicafe.flight.domain.entity.EmailVerification;
import com.afgicafe.flight.domain.entity.User;
import com.afgicafe.flight.domain.enums.Status;
import com.afgicafe.flight.dto.request.RegisterRequest;
import com.afgicafe.flight.email.service.EmailService;
import com.afgicafe.flight.event.publisher.EmailEventPublisher;
import com.afgicafe.flight.mapper.UserMapper;
import com.afgicafe.flight.repository.EmailVerificationRepository;
import com.afgicafe.flight.repository.UserRepository;
import com.afgicafe.flight.security.JwtService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import(TestSecurityConfig.class)
class EmailVerificationControllerTest {

    @MockitoBean private UserSeeder userSeeder;
    @MockitoBean private AuthenticationManager authenticationManager;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private EmailEventPublisher emailEventPublisher;
    @MockitoBean private JavaMailSender javaMailSender;
    @MockitoBean private EmailService emailService;

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private EmailVerificationRepository verificationRepository;
    @Autowired private UserMapper userMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .phoneNumber("+2347023456789")
                .password("Password@123")
                .build();

        testUser = userMapper.toEntity(request);
        testUser.setStatus(Status.PENDING);
        userRepository.saveAndFlush(testUser);
    }

    @Test
    void shouldVerifyEmailSuccessfully() throws Exception {
        EmailVerification verification = new EmailVerification(testUser);
        verificationRepository.saveAndFlush(verification);

        mockMvc.perform(get("/api/v1/auth/verify-email")
                        .param("token", verification.getToken()))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(updatedUser.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(updatedUser.getEmailVerifiedAt()).isNotNull();

        EmailVerification updatedToken = verificationRepository
                .findByToken(verification.getToken())
                .orElseThrow();

        assertThat(updatedToken.isVerified()).isTrue();
    }

    @Test
    void shouldFailWhenTokenIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/auth/verify-email")
                        .param("token", "non-existent-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFailWhenTokenIsExpired() throws Exception {
        EmailVerification expiredToken = new EmailVerification(testUser);
        expiredToken.setExpiresAt(LocalDateTime.now().minusHours(1));
        verificationRepository.saveAndFlush(expiredToken);

        mockMvc.perform(get("/api/v1/auth/verify-email")
                        .param("token", expiredToken.getToken()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailWhenAlreadyVerified() throws Exception {
        EmailVerification verifiedToken = new EmailVerification(testUser);
        verifiedToken.setVerified(true);
        verificationRepository.saveAndFlush(verifiedToken);

        mockMvc.perform(get("/api/v1/auth/verify-email")
                        .param("token", verifiedToken.getToken()))
                .andExpect(status().isBadRequest());
    }
}