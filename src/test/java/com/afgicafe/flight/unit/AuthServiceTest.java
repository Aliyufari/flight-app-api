package com.afgicafe.flight.unit;

import com.afgicafe.flight.domain.entity.RefreshToken;
import com.afgicafe.flight.domain.entity.Role;
import com.afgicafe.flight.domain.entity.User;
import com.afgicafe.flight.domain.enums.RoleEnum;
import com.afgicafe.flight.domain.enums.Status;
import com.afgicafe.flight.dto.request.LoginRequest;
import com.afgicafe.flight.dto.request.RefreshRequest;
import com.afgicafe.flight.dto.request.RegisterRequest;
import com.afgicafe.flight.dto.response.LoginResponse;
import com.afgicafe.flight.exception.UnauthorizedException;
import com.afgicafe.flight.mapper.UserMapper;
import com.afgicafe.flight.repository.RoleRepository;
import com.afgicafe.flight.repository.UserRepository;
import com.afgicafe.flight.security.JwtService;
import com.afgicafe.flight.service.EmailVerificationService;
import com.afgicafe.flight.service.RefreshTokenService;
import com.afgicafe.flight.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder encoder;
    @Mock private AuthenticationManager authManager;
    @Mock private JwtService jwtService;
    @Mock private EmailVerificationService emailVerificationService;
    @Mock private RefreshTokenService refreshTokenService;
    @Mock private UserMapper mapper;

    @InjectMocks
    private AuthServiceImpl service;

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("johndoe@email.com");
        request.setPhoneNumber("08012345678");
        request.setPassword("password");

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(any())).thenReturn(false);

        Role role = new Role();
        when(roleRepository.findByName(RoleEnum.CUSTOMER))
                .thenReturn(Optional.of(role));

        User user = new User();
        when(mapper.toEntity(request)).thenReturn(user);

        when(userRepository.save(any())).thenReturn(user);

        service.register(request);

        verify(userRepository).save(any());
        verify(emailVerificationService).createVerification(any());
    }

    @Test
    void shouldFailLoginWhenPasswordWrong() {
        LoginRequest request = new LoginRequest("johndoe@email.com", "pwd");

        User user = new User();
        user.setStatus(Status.ACTIVE);
        user.setFailedLoginAttempts(0);

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(authManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad Credentials"));

        assertThrows(UnauthorizedException.class, () -> service.login(request));

        verify(userRepository).save(any());
    }

    @Test
    void shouldRefreshTokenSuccessfully() {
        RefreshRequest request = new RefreshRequest("token");

        User user = new User();
        RefreshToken token = new RefreshToken();
        token.setUser(user);

        when(refreshTokenService.validate("token")).thenReturn(token);
        when(refreshTokenService.create(user)).thenReturn(new RefreshToken());
        when(jwtService.generateAccessToken(any())).thenReturn("access");

        LoginResponse response = service.refresh(request);

        assertNotNull(response.tokens());
    }
}
