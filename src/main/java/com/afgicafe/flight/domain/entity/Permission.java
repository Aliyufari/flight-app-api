package com.afgicafe.flight.domain.entity;

import com.afgicafe.flight.domain.enums.PermissionEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private PermissionEnum name;

    @Column(
            name = "permission_value",
            unique = true,
            nullable = false
    )
    private String value;
}
