package com.insulin.formula;

import com.insulin.functional.CalculateIndex;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.ValueConverter.convertSingleGlucose;
import static com.insulin.formula.ValueConverter.convertSingleInsulin;
import static java.lang.Math.log;

public class Quicki implements CalculateIndex {
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
}
