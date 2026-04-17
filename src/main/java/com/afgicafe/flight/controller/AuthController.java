package com.afgicafe.flight.controller;

import com.afgicafe.flight.dto.request.LoginRequest;
import com.afgicafe.flight.dto.request.RefreshRequest;
import com.afgicafe.flight.dto.request.RegisterRequest;
import com.afgicafe.flight.dto.response.LoginResponse;
import com.afgicafe.flight.service.AuthService;
import com.afgicafe.flight.service.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

    @Operation(
            summary = "User registration",
            description = "User registration route"
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register (@Valid @RequestBody RegisterRequest request) {
        service.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                            HttpStatus.CREATED,
                            "Registered successfully, please check your email to verify your account.",
                            null
                        )
                );
    }

    @Operation(
            summary = "User login",
            description = "User login route"
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login (@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "User logged in successfully",
                        service.login(request)
                )
        );
    }

    @Operation(
            summary = "Refresh token",
            description = "Refresh token route"
    )
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh (@Valid @RequestBody RefreshRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Token refreshed successfully",
                        service.refresh(request)
                )
        );
    }
}
