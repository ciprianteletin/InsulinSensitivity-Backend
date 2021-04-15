package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.RangeChecker.checkUpperBound;
import static com.insulin.formula.ValueConverter.*;
import static com.insulin.validation.FormulaValidation.validateTrygliceride;
import static java.lang.Math.log;
import static java.lang.Math.pow;

public class McAuley implements CalculateIndex, IndexInterpreter {
    private static final double upperValue = 5.8;

    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        validateTrygliceride(mandatoryInformation.getOptionalInformation(), "mcAuley");
        double fastingInsulin = mandatoryInformation
                .getInsulinMandatory()
                .getFastingInsulin();
        double trygliceride = mandatoryInformation
                .getOptionalInformation()
                .getTriglyceride();
        fastingInsulin = convertSingleInsulin(fastingInsulin,
                mandatoryInformation.getPlaceholders().getInsulinPlaceholder(), "μIU/mL");
        trygliceride = convertTrygliceride(trygliceride,
                mandatoryInformation.getPlaceholders().getGlucosePlaceholder());
        double power = 2.63 - 0.28 * log(fastingInsulin) - 0.31 * log(trygliceride);
        return pow(Math.E, power);
    }

    @Override
    public String interpret(double result) {
        if (checkUpperBound(upperValue, result)) {
            return "Healthy!";
        }
        return "Insulin Resistance";
    }

    @Override
    public String getInterval() {
        return LESS_THAN + upperValue;
    }
}
