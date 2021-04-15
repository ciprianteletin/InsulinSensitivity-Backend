package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.ValueConverter.*;
import static com.insulin.validation.FormulaValidation.validateNefa;
import static java.lang.Math.log;

public class RevisedQuicki implements CalculateIndex {
    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        validateNefa(mandatoryInformation.getOptionalInformation(), "revised quicki");
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
