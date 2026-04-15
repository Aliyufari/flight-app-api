package com.afgicafe.flight.domain.entity;

import com.afgicafe.flight.domain.enums.Currency;
import com.afgicafe.flight.domain.enums.Gender;
import com.afgicafe.flight.domain.enums.RoleEnum;
import com.afgicafe.flight.domain.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_phone", columnList = "phone_number")
        }
)
public class User extends SoftDeletableEntity {
    @Column(
            name = "first_name",
            nullable = false,
            length = 100
    )
    private String firstName;

    @Column(
            name = "middle_name",
            nullable = true,
            length = 100
    )
    private String middleName;

    @Column(
            name = "last_name",
            nullable = false,
            length = 120
    )
    private String lastName;

    @Column(
            name = "avatar_url",
            nullable = true,
            length = 200
    )
    private String avatarUrl;

    @Column(
            nullable = false,
            length = 150,
            unique = true
    )
    @NaturalId
    private String email;

    @Column(
            name = "phone_number",
            nullable = false,
            length = 20,
            unique = true
    )
    private String phoneNumber;

    @Column(
            name = "email_verified_at",
            nullable = true
    )
    private LocalDateTime emailVerifiedAt;

    @Column(
            name = "failed_login_attempts",
            nullable = false
    )
    private Integer failedLoginAttempts = 0;

    @Column(
            name = "locked_until",
            nullable = true
    )
    private LocalDateTime lockedUntil;

    @Column(
            name = "last_login_at",
            nullable = true
    )
    private LocalDateTime lastLoginAt;

    @Column(
            name = "preferred_currency",
            nullable = true
    )
    @Enumerated(EnumType.STRING)
    private Currency preferredCurrency;

    @Column(
            nullable = true,
            length = 10
    )
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(
            name = "date_of_birth",
            nullable = true
    )
    private LocalDate dateOfBirth;

    @ManyToMany(targetEntity = RoleEnum.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(
            nullable = false,
            length = 255
    )
    private String password;
}
