package com.afgicafe.flight.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionEnum {

    VIEW_PERMISSION("view:permission"),

    VIEW_ROLE("view:role"),
    CREATE_ROLE("create:role"),
    UPDATE_ROLE("update:role"),
    DELETE_ROLE("delete:role"),

    VIEW_USER("view:user"),
    CREATE_USER("create:user"),
    UPDATE_USER("update:user"),
    DELETE_USER("delete:user");

    private final String value;

    public static PermissionEnum fromValue (String value){
        for(PermissionEnum permissionEnum : values()){
            if (permissionEnum.value.equals(value))
                return permissionEnum;
        }

        throw new IllegalArgumentException("Unknown Permission: " + value);
    }
}
