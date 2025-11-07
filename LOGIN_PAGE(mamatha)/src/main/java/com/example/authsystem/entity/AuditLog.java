package com.example.authsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String event; // LOGIN_SUCCESS, LOGIN_FAILURE, LOGOUT
    private String ip;
    private String userAgent;
    private LocalDateTime eventTime = LocalDateTime.now();
    @Column(length = 2000)
    private String details; // optional
}
