package com.insulin.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.apache.logging.log4j.util.Strings.isEmpty;

/**
 * The validator of the phone number, using phonenumbers library for validation. Works only for Romania right now.
 */
//TODO add more countries.
public class PhoneNumberValidator implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (isEmpty(phoneNumber)) {
            return false;
        }
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneRo = phoneNumberUtil.parse(phoneNumber,
                    "RO");
            return phoneNumberUtil.isValidNumber(phoneRo) || phoneNumberUtil.isPossibleNumber(phoneRo);
        } catch (NumberParseException e) {
            return false;
        }
    }
}
