package com.afgicafe.flight.service.impl;

import com.afgicafe.flight.domain.entity.Role;
import com.afgicafe.flight.domain.entity.User;
import com.afgicafe.flight.domain.enums.RoleEnum;
import com.afgicafe.flight.domain.enums.Status;
import com.afgicafe.flight.dto.request.LoginRequest;
import com.afgicafe.flight.dto.request.RegisterRequest;
import com.afgicafe.flight.dto.response.LoginResponse;
import com.afgicafe.flight.email.service.EmailService;
import com.afgicafe.flight.exception.AccountLockedException;
import com.afgicafe.flight.exception.BadRequestException;
import com.afgicafe.flight.exception.ResourceNotFoundException;
import com.afgicafe.flight.exception.UnauthorizedException;
import com.afgicafe.flight.mapper.UserMapper;
import com.afgicafe.flight.repository.EmailVerificationRepository;
import com.afgicafe.flight.repository.RoleRepository;
import com.afgicafe.flight.repository.UserRepository;
import com.afgicafe.flight.security.JwtService;
import com.afgicafe.flight.security.UserPrincipal;
import com.afgicafe.flight.service.AuthService;
import com.afgicafe.flight.service.EmailVerificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final EmailVerificationService emailVerificationService;

    @Override
    @Transactional
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new BadRequestException("Email already exists");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new BadRequestException("Phone number already exists");
        }

        Role role = roleRepository.findByName(RoleEnum.CUSTOMER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        User user = mapper.toEntity(request);
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setPhoneNumber(request.getPhoneNumber().trim());
        user.setRoles(Set.of(role));
        user.setStatus(Status.PENDING);
        user.setPassword(encoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        emailVerificationService.createVerification(savedUser);
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {

        String email = request.getEmail().toLowerCase().trim();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (user.getLockedUntil() != null &&
                user.getLockedUntil().isAfter(LocalDateTime.now())) {
            throw new AccountLockedException("Account is locked. Try again later.");
        }

        if (user.getStatus() != Status.ACTIVE) {
            throw new UnauthorizedException("Please verify your email before logging in");
        }

        try {
            Authentication authenticate = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.getPassword()
                    )
            );

            UserPrincipal principal = (UserPrincipal) authenticate.getPrincipal();

            user.setLastLoginAt(LocalDateTime.now());
            user.setFailedLoginAttempts(0);
            user.setLockedUntil(null);

            userRepository.save(user);

            final String accessToken = jwtService.generateAccessToken(principal);

            return LoginResponse.builder()
                    .user(mapper.toResponse(user))
                    .accessToken(accessToken)
                    .build();

        } catch (BadCredentialsException ex) {

            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);

            if (attempts >= 5) {
                user.setLockedUntil(LocalDateTime.now().plusMinutes(15));
            }

            userRepository.save(user);

            throw new UnauthorizedException("Invalid credentials");
        }
    }
}
