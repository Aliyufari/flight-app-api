package com.afgicafe.flight.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    ADMIN(
            List.of(
                    PermissionEnum.VIEW_PERMISSION,

                    PermissionEnum.VIEW_ROLE,
                    PermissionEnum.CREATE_ROLE,
                    PermissionEnum.UPDATE_ROLE,
                    PermissionEnum.DELETE_ROLE,

                    PermissionEnum.VIEW_USER,
                    PermissionEnum.CREATE_USER,
                    PermissionEnum.UPDATE_USER,
                    PermissionEnum.DELETE_USER
            )
    ),
    CUSTOMER(List.of());

    private final List<PermissionEnum> permissionEnums;
}
