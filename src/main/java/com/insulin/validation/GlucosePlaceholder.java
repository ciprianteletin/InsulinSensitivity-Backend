package com.insulin.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Used for glucose placeholders
 */
@Documented
@Constraint(validatedBy = GlucosePlaceholderValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GlucosePlaceholder {
    String locale() default "";

    String message() default "Incorrect Glucose Placeholder!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
