package com.insulin.formula;

import com.insulin.functional.CalculateIndex;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.ValueConverter.convertSingleInsulin;
import static com.insulin.formula.ValueConverter.convertTrygliceride;
import static java.lang.Math.log;
import static java.lang.Math.pow;

public class McAuley implements CalculateIndex {
    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
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
}
