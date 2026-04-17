package com.afgicafe.flight.controller;

import com.afgicafe.flight.dto.request.ResendVerificationRequest;
import com.afgicafe.flight.service.EmailVerificationService;
import com.afgicafe.flight.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Verification")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class EmailVerificationController {
    private final EmailVerificationService service;

    @Operation(
            summary = "Verify email",
            description = "Verify email route"
    )
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<?>> verifyEmail (@RequestParam("token") String token) {

        service.verifyToken(token);

        return ResponseEntity.ok(
                ApiResponse.ok("Email verified successfully")
        );
    }

    @Operation(
            summary = "Resend verification",
            description = "Resend verification route"
    )
    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<Void>> resend(@RequestBody ResendVerificationRequest request) {

        service.resendVerification(request);

        return ResponseEntity.ok(
                ApiResponse.ok("Verification email resent successfully")
        );
    }
}
