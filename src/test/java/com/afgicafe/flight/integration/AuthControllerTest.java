package com.afgicafe.flight.integration;

import com.afgicafe.flight.config.TestSecurityConfig;
import com.afgicafe.flight.domain.entity.RefreshToken;
import com.afgicafe.flight.domain.entity.User;
import com.afgicafe.flight.domain.enums.Status;
import com.afgicafe.flight.dto.request.LoginRequest;
import com.afgicafe.flight.dto.request.RefreshRequest;
import com.afgicafe.flight.dto.request.RegisterRequest;
import com.afgicafe.flight.email.service.impl.EmailServiceImpl;
import com.afgicafe.flight.repository.UserRepository;
import com.afgicafe.flight.service.EmailVerificationService;
import com.afgicafe.flight.service.RefreshTokenService;
import com.afgicafe.flight.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    @MockitoBean private EmailVerificationService emailVerificationService;
    @MockitoBean private RefreshTokenService refreshTokenService;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private AuthenticationManager authenticationManager;
    @MockitoBean private JavaMailSender javaMailSender;
    @MockitoBean private EmailServiceImpl emailServiceImpl;

    private static final String PASSWORD = "Password@123";

    @BeforeEach
    void setup() {
        when(authenticationManager.authenticate(any()))
                .thenReturn(mock(org.springframework.security.core.Authentication.class));
    }

    @Test
    void shouldRegisterUser() throws Exception {
        doNothing().when(emailVerificationService).createVerification(any());

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldFailRegisterWhenFieldsAreMissing() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegisterRequest())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.error.email").exists())
                .andExpect(jsonPath("$.error.password").exists())
                .andExpect(jsonPath("$.error.first_name").exists())
                .andExpect(jsonPath("$.error.last_name").exists())
                .andExpect(jsonPath("$.error.phone_number").exists());
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        String email = registerAndActivateUser("johnsmith@email.com", "08012345678");

        when(jwtService.generateAccessToken(any())).thenReturn("access-token");
        when(refreshTokenService.create(any(User.class))).thenReturn(mockRefreshToken());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                LoginRequest.builder()
                                        .email(email)
                                        .password(PASSWORD)
                                        .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tokens.access_token").value("access-token"))
                .andExpect(jsonPath("$.data.tokens.refresh_token").exists());
    }

    @Test
    void shouldRefreshTokenSuccessfully() throws Exception {
        registerAndActivateUser("john@email.com", "08087654321");

        when(refreshTokenService.validate("valid-refresh-token")).thenReturn(mockRefreshToken());
        when(refreshTokenService.create(any(User.class))).thenReturn(mockRefreshToken());
        when(jwtService.generateAccessToken(any())).thenReturn("new-access-token");

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                RefreshRequest.builder()
                                        .refreshToken("valid-refresh-token")
                                        .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tokens.access_token").value("new-access-token"))
                .andExpect(jsonPath("$.data.tokens.refresh_token").exists());
    }

    @Test
    void shouldFailRefreshWithInvalidToken() throws Exception {
        when(refreshTokenService.validate("invalid-token"))
                .thenThrow(new com.afgicafe.flight.exception.UnauthorizedException("Invalid refresh token"));

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                RefreshRequest.builder()
                                        .refreshToken("invalid-token")
                                        .build())))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid refresh token"));
    }

    @Test
    void shouldFailRefreshWhenTokenIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                RefreshRequest.builder()
                                        .refreshToken("")
                                        .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.error.refresh_token").exists());
    }

    // --- helpers ---
    private String registerAndActivateUser(String email, String phone) throws Exception {
        doNothing().when(emailVerificationService).createVerification(any());

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterRequest(email, phone))))
                .andExpect(status().isCreated());

        User user = userRepository.findByEmail(email).orElseThrow();
        user.setStatus(Status.ACTIVE);
        userRepository.saveAndFlush(user);

        return email;
    }

    private RegisterRequest validRegisterRequest() {
        return validRegisterRequest("johndoe@email.com", "08012345678");
    }

    private RegisterRequest validRegisterRequest(String email, String phone) {
        return RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email(email)
                .phoneNumber(phone)
                .password(PASSWORD)
                .build();
    }

    private RefreshToken mockRefreshToken() {
        RefreshToken token = new RefreshToken();
        token.setToken("refresh-token");
        token.setUser(new User());
        return token;
    }
}