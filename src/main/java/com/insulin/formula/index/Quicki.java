package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.RangeChecker.checkInBetween;
import static com.insulin.formula.RangeChecker.checkLowerBound;
import static com.insulin.formula.ValueConverter.*;
import static java.lang.Math.log;

public class Quicki implements CalculateIndex, IndexInterpreter {
    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        double fastingGlucose = convertSingleGlucose(
                mandatoryInformation.getGlucoseMandatory().getFastingGlucose(),
                mandatoryInformation.getPlaceholders().getGlucosePlaceholder(),
                "mmol/L");
        double fastingInsulin = convertSingleInsulin(
                mandatoryInformation.getInsulinMandatory().getFastingInsulin(),
                mandatoryInformation.getPlaceholders().getInsulinPlaceholder(),
                "μIU/mL");

        return 1.0 / (log(fastingGlucose) + log(fastingInsulin));
    }

    @Override
    public String interpret(double result) {
        if (checkLowerBound(0.45, result)) {
            return "Healthy";
        }
        if (checkInBetween(0.30, 0.45, result)) {
            return "Insulin Resistance";
        }
        return "Diabetes";
    }

    @Override
    public String getInterval() {
        return GREATER_THAN + 0.45;
    }
}
