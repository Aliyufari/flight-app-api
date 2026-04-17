package com.afgicafe.flight.controller;

import com.afgicafe.flight.service.EmailVerificationService;
import com.afgicafe.flight.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class EmailVerificationController {
    private final EmailVerificationService emailVerificationService;

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<?>> verifyEmail (@RequestParam("token") String token) {

        emailVerificationService.verifyToken(token);

        return ResponseEntity.ok(
                ApiResponse.ok("Email verified successfully")
        );
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<Void>> resend(@RequestParam String email) {

        emailVerificationService.resendVerification(email);

        return ResponseEntity.ok(
                ApiResponse.ok("Verification email resent successfully")
        );
    }
}
