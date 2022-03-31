package com.vet24.models.annotation;


import com.vet24.models.user.User;
import org.springframework.security.core.context.SecurityContextHolder;


import javax.persistence.PostUpdate;

import java.lang.reflect.Field;


public class UpdateAuthorHandler {


    @PostUpdate
    public void methodCaller(Object obj) {
        User activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (Field fields : obj.getClass().getDeclaredFields()) {
            if (fields.isAnnotationPresent(UpdateAuthor.class)) {
                try {
                    fields.setAccessible(true);
                    fields.set(obj, activeUser);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
