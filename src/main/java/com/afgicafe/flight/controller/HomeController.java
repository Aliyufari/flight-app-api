package com.afgicafe.flight.controller;

import com.afgicafe.flight.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<ApiResponse<?>> index () {
        return ResponseEntity.ok(
                ApiResponse.ok("Blink Flight API")
        );
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<?>> health() {
        return ResponseEntity.ok(
                ApiResponse.ok("OK")
        );
    }
}
