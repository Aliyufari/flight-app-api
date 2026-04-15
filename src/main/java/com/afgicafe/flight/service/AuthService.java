package com.afgicafe.flight.service;

import com.afgicafe.flight.dto.request.LoginRequest;
import com.afgicafe.flight.dto.request.RegisterRequest;
import com.afgicafe.flight.dto.response.LoginResponse;
import com.afgicafe.flight.dto.response.UserResponse;

public interface AuthService {

    UserResponse register (RegisterRequest request);

    LoginResponse login (LoginRequest request);
}
