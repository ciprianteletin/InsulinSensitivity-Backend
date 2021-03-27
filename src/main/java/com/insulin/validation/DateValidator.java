package com.insulin.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateValidator implements ConstraintValidator<BirthDay, String> {
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    @Override
    public void initialize(BirthDay constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        DateFormat simpleDate = new SimpleDateFormat(DATE_FORMAT);
        simpleDate.setLenient(false);
        try {
            simpleDate.parse(value);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
