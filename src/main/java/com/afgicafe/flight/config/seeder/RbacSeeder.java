package com.afgicafe.flight.config.seeder;

import com.afgicafe.flight.domain.entity.Permission;
import com.afgicafe.flight.domain.entity.Role;
import com.afgicafe.flight.domain.enums.PermissionEnum;
import com.afgicafe.flight.domain.enums.RoleEnum;
import com.afgicafe.flight.repository.PermissionRepository;
import com.afgicafe.flight.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RbacSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Seeding RBAC");
        seedPermissions();
        seedRoles();
    }

    private void seedPermissions() {
        for (PermissionEnum permissionEnum : PermissionEnum.values()) {
            permissionRepository.findByName(permissionEnum)
                    .orElseGet(() -> {
                        Permission permission = new Permission();
                        permission.setName(permissionEnum);
                        permission.setValue(permissionEnum.getValue());
                        return permissionRepository.save(permission);
                    });
        }
    }

    private void seedRoles() {
        Map<PermissionEnum, Permission> permissionMap = permissionRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Permission::getName, p -> p));

        for (RoleEnum roleEnum : RoleEnum.values()) {
            Role role = roleRepository.findByName(roleEnum)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName(roleEnum);
                        return roleRepository.save(newRole);
                    });

            Set<Permission> permissions = new HashSet<>();

            roleEnum.getPermissionEnums().forEach(permissionEnum -> {
                Permission permission = permissionRepository
                        .findByName(permissionEnum)
                        .orElseThrow(() -> new RuntimeException("Permission not found"));

                permissions.add(permission);
            });

            role.setPermissions(permissions);
            roleRepository.save(role);
        }
    }
}