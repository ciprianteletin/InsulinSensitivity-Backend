package com.insulin.validation;

import com.insulin.exception.model.InputIndexException;
import com.insulin.model.form.OptionalInsulinInformation;

import java.text.MessageFormat;

import static java.util.Objects.isNull;

public final class FormulaValidation {
    private static final String SINGLE_NULL = "Expected value for field {0} used for index {1}";
    private static final String DOUBLE_VALUES = "Null value detected for {0} or {1} for index {2}";

    private FormulaValidation() {

    }

    public static void validateWeightAndHeight(OptionalInsulinInformation optionalInformation, String index)
            throws InputIndexException {
        validateHeight(optionalInformation, index);
        validateWeight(optionalInformation, index);
    }

    public static void validateWeight(OptionalInsulinInformation optionalInformation, String index)
            throws InputIndexException {
        Integer weight = optionalInformation.getWeight();
        if (isNull(weight)) {
            String message = MessageFormat.format(SINGLE_NULL, "weight", index);
            throw new InputIndexException(message);
        }
    }

    public static void validateHeight(OptionalInsulinInformation optionalInformation, String index)
            throws InputIndexException {
        Integer height = optionalInformation.getWeight();
        if (isNull(height)) {
            String message = MessageFormat.format(SINGLE_NULL, "height", index);
            throw new InputIndexException(message);
        }
    }

    public static void validateNefa(OptionalInsulinInformation optionalInformation, String index)
            throws InputIndexException {
        Double nefa = optionalInformation.getNefa();
        if (isNull(nefa)) {
            String message = MessageFormat.format(SINGLE_NULL, "nefa", index);
            throw new InputIndexException(message);
        }
    }

    public static void validateTrygliceride(OptionalInsulinInformation optionalInformation, String index)
            throws InputIndexException {
        Double trygliceride = optionalInformation.getTriglyceride();
        if (isNull(trygliceride)) {
            String message = MessageFormat.format(SINGLE_NULL, "trygliceride", index);
            throw new InputIndexException(message);
        }
    }

    public static void validateTyroAndHdl(OptionalInsulinInformation optionalInformation, String index)
            throws InputIndexException {
        Double tyroglobulin = optionalInformation.getThyroglobulin();
        Double hdl = optionalInformation.getHdl();
        if (isNull(tyroglobulin) || isNull(hdl)) {
            String message = MessageFormat.format(DOUBLE_VALUES, "tyroglobulin", "hdl", index);
            throw new InputIndexException(message);
        }
    }
}
