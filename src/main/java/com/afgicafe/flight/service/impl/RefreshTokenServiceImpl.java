package com.afgicafe.flight.service.impl;

import com.afgicafe.flight.domain.entity.RefreshToken;
import com.afgicafe.flight.domain.entity.User;
import com.afgicafe.flight.exception.UnauthorizedException;
import com.afgicafe.flight.repository.RefreshTokenRepository;
import com.afgicafe.flight.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository repository;

    @Override
    public RefreshToken create(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(generateSecureToken());
        token.setExpiresAt(LocalDateTime.now().plusDays(7));
        return repository.save(token);
    }

    @Override
    public RefreshToken validate(String token) {
        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException(("Invalid refresh token")));

        if (refreshToken.isExpired()) {
            repository.delete(refreshToken);
            throw new UnauthorizedException("Refresh token expired");
        }

        return refreshToken;
    }

    @Override
    public void deleteByUser(User user) {
        repository.deleteByUser(user);
    }

    private String generateSecureToken () {
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding().encodeToString(randomBytes);
    }
}
