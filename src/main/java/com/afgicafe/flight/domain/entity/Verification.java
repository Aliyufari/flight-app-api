package com.afgicafe.flight.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Table(name = "verifications")
public class Verification extends BaseEntity {
    @Column(nullable = false)
    private String token;

    @Column(name = "created_at", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createdAt;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public Verification (User user) {
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.token = UUID.randomUUID().toString();
    }
}
