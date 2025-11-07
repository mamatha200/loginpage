package com.example.authsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_roles",
       joinColumns = @JoinColumn(name="user_id"),
       inverseJoinColumns = @JoinColumn(name="role_name"))
    private Set<Role> roles;

    private boolean accountNonLocked = true;
    private LocalDateTime lockTime;

    private LocalDateTime createdAt = LocalDateTime.now();
}
