package com.vet24.models.util;

import com.vet24.models.annotation.CreateAuthor;
import com.vet24.models.annotation.UpdateAuthor;
import com.vet24.models.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;

public class ReflectionUtil {
    public void searchAnnotationUpdateAuthor(Class currentClass, Object entity) {
        for (Field fields : currentClass.getDeclaredFields()) {
            if (fields.isAnnotationPresent(UpdateAuthor.class)) {
                try {
                    User activeUser = getSecurityUserOrNull();
                    fields.setAccessible(true);
                    fields.set(entity, activeUser);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!currentClass.equals(Object.class)) {
            currentClass = currentClass.getSuperclass();
            searchAnnotationUpdateAuthor(currentClass, entity);
        }
    }

    public void searchAnnotationCreateAuthor(Class currentClass, Object entity) {
        for (Field fields : currentClass.getDeclaredFields()) {
            if (fields.isAnnotationPresent(CreateAuthor.class)) {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    continue;
                }
                try {
                    User activeUser = getSecurityUserOrNull();
                    fields.setAccessible(true);
                    fields.set(entity, activeUser);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!currentClass.equals(Object.class)) {
            currentClass = currentClass.getSuperclass();
            searchAnnotationCreateAuthor(currentClass, entity);
        }
    }
}
