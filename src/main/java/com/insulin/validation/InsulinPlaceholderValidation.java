package com.insulin.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InsulinPlaceholderValidation implements ConstraintValidator<InsulinPlaceholder, String> {
    @Override
    public void initialize(InsulinPlaceholder constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.equals("pmol/L") || value.equals("μIU/mL");
    }
}
