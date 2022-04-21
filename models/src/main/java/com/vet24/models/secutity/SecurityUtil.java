package com.vet24.models.secutity;

import com.vet24.models.user.User;
import org.springframework.security.core.context.SecurityContextHolder;


public class SecurityUtil {
    public static User getSecurityUserOrNull() {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }
}
