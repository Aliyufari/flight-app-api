package com.afgicafe.flight.mapper;

import com.afgicafe.flight.domain.entity.Role;
import com.afgicafe.flight.dto.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {PermissionMapper.class}
)
public interface RoleMapper {
    RoleResponse toResponse (Role role);
}
