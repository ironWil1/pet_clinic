package com.vet24.security.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ADMIN")) {
            response.sendRedirect("/api/admin");
        } else if (roles.contains("MANAGER")) {
            response.sendRedirect("/api/manager");
        } else if (roles.contains("DOCTOR")) {
            response.sendRedirect("/api/doctor");
        } else if (roles.contains("CLIENT")) {
            response.sendRedirect("/api/client");
        } else {
            response.sendRedirect("/api/client");
        }
    }
}