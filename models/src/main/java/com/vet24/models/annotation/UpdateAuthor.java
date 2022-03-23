package com.vet24.models.annotation;


import java.lang.annotation.*;

@Documented
@Target({ElementType.LOCAL_VARIABLE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateAuthor {

}
