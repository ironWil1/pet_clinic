package com.vet24.models.annotation;


import com.vet24.models.user.User;
import org.springframework.security.core.context.SecurityContextHolder;


import javax.persistence.PreUpdate;

import java.lang.reflect.Field;


public class UpdateAuthorHandler {

    User activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    @PreUpdate
    public void methodCaller(Object obj) {
        for (Field fields : obj.getClass().getDeclaredFields()) {
            if (fields.isAnnotationPresent(UpdateAuthor.class)) {
                try {
                    fields.set(obj, activeUser);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
