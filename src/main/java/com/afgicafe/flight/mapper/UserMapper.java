package com.afgicafe.flight.mapper;

import com.afgicafe.flight.domain.entity.User;
import com.afgicafe.flight.dto.request.RegisterRequest;
import com.afgicafe.flight.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {RoleMapper.class}
)
public interface UserMapper {
    User toEntity (RegisterRequest request);

    UserResponse toResponse (User user);
}
