package com.afgicafe.flight.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenResponse(
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6kpXVCJ9...")
        @JsonProperty("access_token")
        String accessToken,

        @Schema(example = "2c6zQDbiVEqoes8T4iv7kVYs9sO6zlBL2qW...")
        @JsonProperty("refresh_token")
        String refreshToken
) {}
