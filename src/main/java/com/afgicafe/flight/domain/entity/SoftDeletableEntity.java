package com.afgicafe.flight.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class SoftDeletableEntity extends AuditableEntity {
    @Column(
            name = "deleted_at",
            insertable = false
    )
    private LocalDateTime deletedAt;
}
