package com.vet24.models.util;

import com.vet24.models.user.User;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;

public class ReflectionUtil {
    public <T> void searchAnnotationAuthor(Class currentClass, Object entity, Class<T> updateAuthorClass) {
        for (Field fields : currentClass.getDeclaredFields()) {
            if (fields.isAnnotationPresent((Class<? extends Annotation>) updateAuthorClass)) {
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
            searchAnnotationAuthor(currentClass, entity, updateAuthorClass);
        }
    }
}
