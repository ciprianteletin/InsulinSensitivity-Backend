package com.insulin.functional;

import com.insulin.model.form.GlucoseMandatory;

/**
 * Interface to interpret and find the result of index calculation,
 * or find a result based on user's data. It was created with the pure
 * intention of avoiding multiple if else statements (> 10).
 * <p>
 * It needs the GlucoseMandatory object which contains all glucose information
 * inserted by the user. It does not need the placeholder for the conversion,
 * as the values that are send here will be converted in mg/dL unit beforehand.
 */
public interface Interpreter {
    boolean interpret(GlucoseMandatory glucoseMandatory);
    String getName();
}
