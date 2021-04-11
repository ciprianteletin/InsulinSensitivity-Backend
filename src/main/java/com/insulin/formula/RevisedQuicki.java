package com.insulin.formula;

import com.insulin.functional.CalculateIndex;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.ValueConverter.*;
import static java.lang.Math.log;

public class RevisedQuicki implements CalculateIndex {
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
        double nefa = convertNefa(mandatoryInformation.getOptionalInformation().getNefa(),
                mandatoryInformation.getPlaceholders().getGlucosePlaceholder());

        return 1.0 / (log(fastingGlucose) + log(fastingInsulin) + log(nefa));
    }
}
