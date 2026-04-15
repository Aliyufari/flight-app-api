package com.afgicafe.flight.dto.response;

import com.afgicafe.flight.domain.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoleResponse(
        UUID id,

        RoleEnum name,

        Set<PermissionResponse> permissions
) {}
