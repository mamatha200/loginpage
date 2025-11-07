package com.example.authsystem.config;

import com.example.authsystem.service.AuditService;
import com.example.authsystem.service.LoginAttemptService;
import com.example.authsystem.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthHandlers {

    public static class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
        private final AuditService audit;
        private final LoginAttemptService attemptService;
        private final UserService userService;

        public AuthSuccessHandler(AuditService audit, LoginAttemptService attemptService, UserService userService) {
            this.audit = audit; this.attemptService = attemptService; this.userService = userService;
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
            String username = authentication.getName();
            attemptService.loginSucceeded(username);
            audit.log(username, "LOGIN_SUCCESS", request, "Successful login");
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    public static class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
        private final AuditService audit;
        private final LoginAttemptService attemptService;

        public AuthFailureHandler(AuditService audit, LoginAttemptService attemptService) {
            this.audit = audit; this.attemptService = attemptService;
        }

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {
            String username = request.getParameter("username");
            if (username != null) attemptService.loginFailed(username);
            audit.log(username != null ? username : "unknown", "LOGIN_FAILURE", request, exception.getMessage());
            super.onAuthenticationFailure(request, response, exception);
        }
    }

    public static class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
        private final AuditService audit;
        public CustomLogoutSuccessHandler(AuditService audit) { this.audit = audit; }

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            String username = authentication != null ? authentication.getName() : "anonymous";
            audit.log(username, "LOGOUT", request, "User logged out");
            response.sendRedirect("/login?logout");
        }
    }
}
