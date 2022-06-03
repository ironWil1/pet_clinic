package com.vet24.models.secutity;

import com.vet24.models.user.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class SecurityUtil {
    public static User getSecurityUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.equals(authentication.getClass())) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }
}
