package com.afgicafe.flight.service;

import com.afgicafe.flight.dto.request.LoginRequest;
import com.afgicafe.flight.dto.request.RefreshRequest;
import com.afgicafe.flight.dto.request.RegisterRequest;
import com.afgicafe.flight.dto.response.LoginResponse;

public interface AuthService {
    void register (RegisterRequest request);

    LoginResponse login (LoginRequest request);

    LoginResponse refresh (RefreshRequest request);
}

