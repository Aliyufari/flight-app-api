package com.afgicafe.flight.controller;

import com.afgicafe.flight.dto.request.LoginRequest;
import com.afgicafe.flight.dto.request.RegisterRequest;
import com.afgicafe.flight.dto.response.LoginResponse;
import com.afgicafe.flight.dto.response.UserResponse;
import com.afgicafe.flight.service.AuthService;
import com.afgicafe.flight.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @Operation(
            summary = "User registration",
            description = "User registration route"
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register (@Valid @RequestBody RegisterRequest request) {
        var response = ApiResponse.success(
                HttpStatus.CREATED,
                "User registered successfully",
                "user",
                service.register(request)
        );

        return ResponseEntity
                .status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "User login",
            description = "User login route"
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login (@Valid @RequestBody LoginRequest request) {
        var response = ApiResponse.success(
                HttpStatus.OK,
                "User logged in successfully",
                "user",
                service.login(request)
        );

        return ResponseEntity.ok(response);
    }
}
