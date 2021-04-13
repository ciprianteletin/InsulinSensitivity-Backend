package com.insulin.formula;

import com.insulin.functional.CalculateIndex;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.ValueConverter.*;
import static com.insulin.utils.FormulaUtils.glucoseMean;
import static com.insulin.utils.FormulaUtils.insulinMean;
import static com.insulin.validation.FormulaValidation.validateWeight;
import static java.lang.Math.log;

public class Cederholm implements CalculateIndex {
    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        validateWeight(mandatoryInformation.getOptionalInformation(), "cederholm");
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();

        glucoseMandatory = glucoseConverter(glucoseMandatory,
                mandatoryInformation.getPlaceholders().getGlucosePlaceholder(), "mmol/L");
        insulinMandatory = insulinConverter(insulinMandatory,
                mandatoryInformation.getPlaceholders().getInsulinPlaceholder(), "μIU/mL");

        double meanGlucose = glucoseMean(glucoseMandatory);
        double meanInsulin = insulinMean(insulinMandatory);
        return (75000 +
                (glucoseMandatory.getFastingGlucose() - glucoseMandatory.getGlucoseOneTwenty())
                        * 1.15 * 180 * 0.19 * mandatoryInformation.getOptionalInformation().getWeight()
        ) / (120 * meanGlucose * log(meanInsulin));
    }
}
