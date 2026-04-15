package com.afgicafe.flight.mapper;

import com.afgicafe.flight.domain.entity.Permission;
import com.afgicafe.flight.dto.response.PermissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PermissionMapper {
    PermissionResponse toResponse (Permission permission);
}
