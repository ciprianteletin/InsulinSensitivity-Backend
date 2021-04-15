package com.insulin.formula.index;

import com.insulin.functional.CalculateIndex;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.shared.NumericConstants.TEN_FOUR;
import static com.insulin.formula.ValueConverter.*;
import static com.insulin.utils.FormulaUtils.glucoseMean;
import static com.insulin.utils.FormulaUtils.insulinMean;

public class Matsuda implements CalculateIndex {
    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();

        glucoseMandatory = glucoseConverter(glucoseMandatory,
                mandatoryInformation.getPlaceholders().getGlucosePlaceholder(), "mg/dL");
        insulinMandatory = insulinConverter(insulinMandatory,
                mandatoryInformation.getPlaceholders().getInsulinPlaceholder(), "μIU/mL");

        double meanGlucose = glucoseMean(glucoseMandatory);
        double meanInsulin = insulinMean(insulinMandatory);

        return TEN_FOUR / (Math.sqrt(glucoseMandatory.getFastingGlucose()) * insulinMandatory.getFastingInsulin()
                * meanGlucose * meanInsulin);
    }
}
