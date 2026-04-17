package com.afgicafe.flight.dto.response;

import com.afgicafe.flight.domain.enums.Gender;
import com.afgicafe.flight.domain.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
        UUID id,

        @Schema(example = "John")
        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("middle_name")
        String middleName,

        @Schema(example = "Doe")
        @JsonProperty("last_name")
        String lastName,

        @JsonProperty("avatar_url")
        String avatarUrl,

        String email,

        @Schema(example = "+2347023456789")
        @JsonProperty("phone_number")
        String phoneNumber,

        @Schema(example = "Male")
        Gender gender,

        @JsonProperty("date_of_birth")
        LocalDate dateOfBirth,

        RoleResponse role,

        Status status
) {}
