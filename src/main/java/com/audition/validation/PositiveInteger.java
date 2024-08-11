package com.audition.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PositiveIntegerValidator.class)
public @interface PositiveInteger {

    String message() default "Must be integer greater than zero";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}