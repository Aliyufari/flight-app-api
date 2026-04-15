package com.afgicafe.flight.dto.response;

import com.afgicafe.flight.domain.enums.PermissionEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PermissionResponse(
        UUID id,
        PermissionEnum name,
        String value
) {}
