package com.example.authsystem;

import com.example.authsystem.entity.Role;
import com.example.authsystem.repo.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepo) {
        return args -> {
            if (!roleRepo.existsById("ROLE_USER")) roleRepo.save(new Role("ROLE_USER"));
            if (!roleRepo.existsById("ROLE_ADMIN")) roleRepo.save(new Role("ROLE_ADMIN"));
        };
    }
}
