package com.vet24.models.annotation;

import java.lang.annotation.*;


@Documented
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateAuthor {

}





