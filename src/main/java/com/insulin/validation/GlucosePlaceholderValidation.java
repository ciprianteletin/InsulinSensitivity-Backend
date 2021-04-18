package com.insulin.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GlucosePlaceholderValidation implements ConstraintValidator<GlucosePlaceholder, String> {
    @Override
    public void initialize(GlucosePlaceholder constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.equals("mmol/L") || value.equals("mg/dL");
    }
}
