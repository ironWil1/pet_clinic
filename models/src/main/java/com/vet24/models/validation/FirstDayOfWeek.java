package com.vet24.models.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FirstDayOfWeekValidator.class)
@Documented
public @interface FirstDayOfWeek {
    String message() default "Current day is not first day of week!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
