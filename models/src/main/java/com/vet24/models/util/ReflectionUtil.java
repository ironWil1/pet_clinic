package com.vet24.models.util;

import com.vet24.models.user.User;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;

public class ReflectionUtil {
    public static Field searchFieldWithAnnotation(Class currentClass, Class<? extends Annotation> annotation) {
        Field field = null;
        for (Field fields : currentClass.getDeclaredFields()) {
            if (fields.isAnnotationPresent(annotation)) {
                return fields;
            }
        }
        if (!currentClass.equals(Object.class)) {
            currentClass = currentClass.getSuperclass();
            field = searchFieldWithAnnotation(currentClass, annotation);
        }
        return field;
    }
}
