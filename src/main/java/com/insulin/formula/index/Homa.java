package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.RangeChecker.checkInBetween;
import static com.insulin.formula.RangeChecker.checkLowerBound;
import static com.insulin.formula.ValueConverter.*;

public class Homa implements CalculateIndex, IndexInterpreter {
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

        return (fastingGlucose * fastingInsulin) / 22.5;
    }

    @Override
    public String interpret(double result) {
        if (checkInBetween(0.5, 1.4, result)) {
            return "Healthy";
        }
        if (checkInBetween(1.9, 2.9, result)) {
            return "Early Insulin Resistance!";
        }
        if (checkLowerBound(2.9, result)) {
            return "Insulin Resistance";
        }
        return "-";
    }

    @Override
    public String getInterval() {
        return "0.5" + DASH + "1.4";
    }
}
