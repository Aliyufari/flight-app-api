package com.afgicafe.flight.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Schema(example = "john@email.com")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(example = "Password@123")
    @NotBlank(message = "Password is required")
    private String password;
}
