package com.afgicafe.flight.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginResponse(
        UserResponse user,

        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6kpXVCJ9...")
        @JsonProperty("access_token")
        String accessToken
) {}
