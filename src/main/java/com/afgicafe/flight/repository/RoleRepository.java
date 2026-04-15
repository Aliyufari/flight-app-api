package com.afgicafe.flight.repository;

import com.afgicafe.flight.domain.entity.Role;
import com.afgicafe.flight.domain.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleEnum name);
}
