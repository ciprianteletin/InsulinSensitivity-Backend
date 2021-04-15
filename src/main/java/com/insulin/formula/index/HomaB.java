package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.ValueConverter.convertSingleGlucose;
import static com.insulin.formula.ValueConverter.convertSingleInsulin;

public class HomaB implements CalculateIndex, IndexInterpreter {
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

        return (20 * fastingInsulin) / (fastingGlucose - 3.5);
    }

    @Override
    public String interpret(double result) {
        return "-";
    }

    @Override
    public String getInterval() {
        return "-";
    }
}
