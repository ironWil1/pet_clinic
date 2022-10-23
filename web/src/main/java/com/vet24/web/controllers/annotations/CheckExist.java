package com.vet24.web.controllers.annotations;


import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckExist {
        Class<?> entityClass();

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
}
