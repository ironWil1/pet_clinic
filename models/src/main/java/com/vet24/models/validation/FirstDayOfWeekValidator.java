package com.vet24.models.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FirstDayOfWeekValidator implements ConstraintValidator<FirstDayOfWeek, LocalDate> {
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null) {
            return false;
        }

        if (localDate.getDayOfWeek().getValue() == 1) {
            return true;
        }
        return false;
    }
}
