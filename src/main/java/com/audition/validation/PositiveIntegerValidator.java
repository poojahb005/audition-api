package com.audition.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PositiveIntegerValidator implements ConstraintValidator<PositiveInteger, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            if (null == value || Integer.parseInt(value) > 0) {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }
}