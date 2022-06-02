package com.vet24.models.util;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ReflectionUtil {
    public static Field searchFieldWithAnnotation(Class currentClass, Class<? extends Annotation> annotation) {
        Field fieldRes = null;
        for (Field field : currentClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotation)) {
                return field;
            }
        }
        if (!currentClass.equals(Object.class)) {
            currentClass = currentClass.getSuperclass();
            fieldRes = searchFieldWithAnnotation(currentClass, annotation);
        }
        return fieldRes;
    }
}
