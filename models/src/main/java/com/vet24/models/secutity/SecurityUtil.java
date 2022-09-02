package com.vet24.models.secutity;

import com.vet24.models.user.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


public class SecurityUtil {
    public static Optional<User> getOptionalOfNullableSecurityUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(auth -> !auth.getClass().equals(AnonymousAuthenticationToken.class))
                .map(Authentication::getPrincipal)
                .map(User.class::cast);
    }

}
