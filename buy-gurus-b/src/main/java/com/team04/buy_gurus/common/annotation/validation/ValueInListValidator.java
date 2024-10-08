package com.team04.buy_gurus.common.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ValueInListValidator implements ConstraintValidator<ValueInList, String> {
    private String[] values;

    @Override
    public void initialize(ValueInList constraintAnnotation) {
        this.values = constraintAnnotation.values();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }
        return Arrays.asList(values).contains(s);
    }
}
