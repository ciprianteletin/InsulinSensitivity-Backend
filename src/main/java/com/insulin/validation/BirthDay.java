package com.insulin.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDay {
    String locale() default "";

    String message() default "Invalid birth date!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
