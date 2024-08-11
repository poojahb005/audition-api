package com.audition.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PositiveIntegerValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    public void testIsValid() {
        PositiveIntegerValidator positiveIntegerValidator = new PositiveIntegerValidator();
        boolean result = positiveIntegerValidator.isValid("1", constraintValidatorContext);
        assertTrue(result);
    }

    @Test
    public void testIsNotValid() {
        PositiveIntegerValidator positiveIntegerValidator = new PositiveIntegerValidator();
        boolean result = positiveIntegerValidator.isValid("1weq", constraintValidatorContext);
        assertFalse(result);
    }

    @Test
    public void testIsNotValidNegativeNumber() {
        PositiveIntegerValidator positiveIntegerValidator = new PositiveIntegerValidator();
        boolean result = positiveIntegerValidator.isValid("-1", constraintValidatorContext);
        assertFalse(result);
    }

}
