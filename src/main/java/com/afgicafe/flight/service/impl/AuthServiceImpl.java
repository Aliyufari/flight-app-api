package com.afgicafe.flight.service.impl;

import com.afgicafe.flight.domain.entity.RefreshToken;
import com.afgicafe.flight.domain.entity.Role;
import com.afgicafe.flight.domain.entity.User;
import com.afgicafe.flight.domain.enums.RoleEnum;
import com.afgicafe.flight.domain.enums.Status;
import com.afgicafe.flight.dto.request.LoginRequest;
import com.afgicafe.flight.dto.request.RefreshRequest;
import com.afgicafe.flight.dto.request.RegisterRequest;
import com.afgicafe.flight.dto.response.LoginResponse;
import com.afgicafe.flight.dto.response.TokenResponse;
import com.afgicafe.flight.exception.AccountLockedException;
import com.afgicafe.flight.exception.BadRequestException;
import com.afgicafe.flight.exception.ResourceNotFoundException;
import com.afgicafe.flight.exception.UnauthorizedException;
import com.afgicafe.flight.mapper.UserMapper;
import com.afgicafe.flight.repository.RoleRepository;
import com.afgicafe.flight.repository.UserRepository;
import com.afgicafe.flight.security.JwtService;
import com.afgicafe.flight.security.UserPrincipal;
import com.afgicafe.flight.service.AuthService;
import com.afgicafe.flight.service.EmailVerificationService;
import com.afgicafe.flight.service.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public void register(RegisterRequest request) {

        if (request.getEmail() == null) {
            throw new BadRequestException("Email is required");
        }

        String email = request.getEmail().toLowerCase().trim();

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already exists");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new BadRequestException("Phone number already exists");
        }

        Role role = roleRepository.findByName(RoleEnum.CUSTOMER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        User user = mapper.toEntity(request);

        user.setEmail(email);
        user.setPhoneNumber(request.getPhoneNumber().trim());
        user.setRole(role);
        user.setStatus(Status.PENDING);
        user.setPassword(encoder.encode(request.getPassword()));

        userRepository.save(user);

        emailVerificationService.createVerification(user);
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

            RefreshToken refreshToken = refreshTokenService.create(user);
            final String accessToken = jwtService.generateAccessToken(principal);

            return LoginResponse.builder()
                    .user(mapper.toResponse(user))
                    .tokens(new TokenResponse(accessToken, refreshToken.getToken()))
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

    @Override
    @Transactional
    public LoginResponse refresh(RefreshRequest request) {

        String tokenValue = request.getRefreshToken();

        if (tokenValue == null || tokenValue.isBlank()) {
            throw new BadRequestException("Token is required");
        }

        RefreshToken oldRefreshToken = refreshTokenService.validate(tokenValue);

        if (oldRefreshToken == null) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        User user = oldRefreshToken.getUser();
        refreshTokenService.deleteByUser(user);

        RefreshToken newRefreshToken = refreshTokenService.create(user);
        if (newRefreshToken == null) {
            throw new IllegalStateException("Failed to generate refresh token");
        }

        UserPrincipal principal = new UserPrincipal(user);
        String accessToken = jwtService.generateAccessToken(principal);

        return LoginResponse.builder()
                .user(mapper.toResponse(user))
                .tokens(new TokenResponse(accessToken, newRefreshToken.getToken()))
                .build();
    }
}
