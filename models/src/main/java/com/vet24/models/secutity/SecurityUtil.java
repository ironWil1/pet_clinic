package com.vet24.models.secutity;

import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

public class SecurityUtil {
    public static Principal getPrincipalOrNull() {

        Principal principal = null;
        try {
            principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return principal;
    }
}

