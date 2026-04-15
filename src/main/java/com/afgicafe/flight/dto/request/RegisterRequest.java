package com.afgicafe.flight.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @Schema(example = "John")
    @NotBlank(message = "First name is required")
    @Size(max = 100)
    @JsonProperty("first_name")
    private String firstName;

    @Schema(example = "Doe")
    @NotBlank(message = "Last name is required")
    @Size(max = 120)
    @JsonProperty("last_name")
    private String lastName;

    @Schema(example = "john@email.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 150)
    private String email;

    @Schema(example = "+2347023456789")
    @NotBlank(message = "Phone number is required")
    @Size(min = 7, max = 20)
    @Pattern(regexp = "^[0-9+]+$", message = "Invalid phone number")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Schema(example = "Password@123")
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be 8–100 characters")
    private String password;
}
