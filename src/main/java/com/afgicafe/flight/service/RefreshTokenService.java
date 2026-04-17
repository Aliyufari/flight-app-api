package com.afgicafe.flight.service;

import com.afgicafe.flight.domain.entity.RefreshToken;
import com.afgicafe.flight.domain.entity.User;

public interface RefreshTokenService {
    RefreshToken create (User user);
    RefreshToken validate (String token);
    void deleteByUser (User user);
}
