package com.example.authsystem.service;

import com.example.authsystem.entity.LoginAttempt;
import com.example.authsystem.entity.User;
import com.example.authsystem.repo.LoginAttemptRepository;
import com.example.authsystem.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginAttemptService {
    private final LoginAttemptRepository repo;
    private final UserRepository userRepo;

    @Value("${app.security.maxFailedAttempts:5}")
    private int MAX_FAILED;

    @Value("${app.security.lockTimeMinutes:15}")
    private int LOCK_MINUTES;

    public LoginAttemptService(LoginAttemptRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public void loginSucceeded(String username) {
        repo.findByUsername(username).ifPresent(la -> repo.delete(la));
    }

    public void loginFailed(String username) {
        LoginAttempt la = repo.findByUsername(username).orElseGet(() -> {
            LoginAttempt n = new LoginAttempt();
            n.setUsername(username);
            n.setAttempts(0);
            return n;
        });
        la.setAttempts(la.getAttempts() + 1);
        la.setLastAttemptAt(LocalDateTime.now());
        repo.save(la);

        if (la.getAttempts() >= MAX_FAILED) {
            userRepo.findByUsername(username).ifPresent(user -> {
                user.setAccountNonLocked(false);
                user.setLockTime(LocalDateTime.now());
                userRepo.save(user);
            });
        }
    }

    public boolean isAccountLocked(User user) {
        if (user == null) return false;
        if (user.isAccountNonLocked()) return false;
        if (user.getLockTime() == null) return true;

        LocalDateTime unlockTime = user.getLockTime().plusMinutes(LOCK_MINUTES);
        if (LocalDateTime.now().isAfter(unlockTime)) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            userRepo.save(user);
            repo.findByUsername(user.getUsername()).ifPresent(repo::delete);
            return false;
        }
        return true;
    }
}
