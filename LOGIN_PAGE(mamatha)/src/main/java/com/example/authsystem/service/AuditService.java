package com.example.authsystem.service;

import com.example.authsystem.entity.AuditLog;
import com.example.authsystem.repo.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private final AuditLogRepository repo;

    public AuditService(AuditLogRepository repo) { this.repo = repo; }

    public void log(String username, String event, HttpServletRequest req, String details) {
        AuditLog a = new AuditLog();
        a.setUsername(username);
        a.setEvent(event);
        a.setIp(req != null ? (req.getHeader("X-Forwarded-For") == null ? req.getRemoteAddr() : req.getHeader("X-Forwarded-For")) : "unknown");
        a.setUserAgent(req != null ? req.getHeader("User-Agent") : null);
        a.setDetails(details);
        repo.save(a);
    }
}
