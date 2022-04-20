package com.vet24.models.secutity;

import com.vet24.models.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

public class SecurityUtil {
    public static User getSecurityUserOrNull() {

        User user = null;
        try {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return user;
    }

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

