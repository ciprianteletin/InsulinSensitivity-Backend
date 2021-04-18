package com.insulin.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Used for insulin placeholders
 */
@Documented
@Constraint(validatedBy = InsulinPlaceholderValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InsulinPlaceholder {
    String locale() default "";

    String message() default "Incorrect Insulin Placeholder!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
