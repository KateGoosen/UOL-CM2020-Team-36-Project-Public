package com.team_36.cm2020.api_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    private String password;

    @Column(nullable = false)
    private LocalDateTime lastTimeActive;

    @Column(nullable = false)
    private LocalDateTime dateTimeToDelete;

    @Column(nullable = false)
    private boolean isRegistered;
}
