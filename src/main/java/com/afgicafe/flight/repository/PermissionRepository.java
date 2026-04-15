package com.afgicafe.flight.repository;

import com.afgicafe.flight.domain.entity.Permission;
import com.afgicafe.flight.domain.enums.PermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByName(PermissionEnum name);
}
