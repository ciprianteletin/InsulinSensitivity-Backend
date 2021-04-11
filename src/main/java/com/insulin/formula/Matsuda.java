package com.insulin.formula;

import com.insulin.functional.CalculateIndex;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.ValueConverter.*;
import static com.insulin.formula.ValueConverter.insulinMean;

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

        return 10000 / (Math.sqrt(glucoseMandatory.getFastingGlucose()) * insulinMandatory.getFastingInsulin()
                * meanGlucose * meanInsulin);
    }
}
