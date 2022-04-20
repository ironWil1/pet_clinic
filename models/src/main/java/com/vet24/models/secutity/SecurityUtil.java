package com.vet24.models.secutity;

import com.vet24.models.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

public class SecurityUtil {
//    public static Principal getPrincipalOrNull() {
//
//        Principal principal = null;
//        try {
//            principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//        return principal;
//    }


        public static User getSecurityUserOrNull() {
//метод должен возвращаться либо объект User либо null, не должен никак и никогда выбрасывать NPE

            User user = null;
            try {
                user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            } catch (NullPointerException e){
                e.printStackTrace();
            }
            return user;
        }


    }

