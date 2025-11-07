package com.example.authsystem.service;
 
import com.example.authsystem.entity.Role;
import com.example.authsystem.entity.User;
import com.example.authsystem.repo.RoleRepository;
import com.example.authsystem.repo.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
 
import java.util.Set;
import java.util.stream.Collectors;
 
@Service
public class UserService implements UserDetailsService {
 
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
 
    public UserService(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }
 
    public User registerUser(String username, String rawPassword) {
        if (userRepo.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
 
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(rawPassword));
 
        // Assign default role
        Role role = roleRepo.findById("ROLE_USER").orElseGet(() -> {
            Role newRole = new Role("ROLE_USER");
            return roleRepo.save(newRole);
        });
 
        u.setRoles(Set.of(role));
        return userRepo.save(u);
    }
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
 
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                u.getRoles().stream()
                        .map(Role::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet())
        );
    }
 
    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }
}