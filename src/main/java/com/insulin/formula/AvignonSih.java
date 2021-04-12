package com.insulin.formula;

import com.insulin.functional.CalculateIndex;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.NumericConstants.TEN_EIGHT;
import static com.insulin.formula.ValueConverter.glucoseConverter;
import static com.insulin.formula.ValueConverter.insulinConverter;

public class AvignonSih implements CalculateIndex {
    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();
        double vd = 150. / mandatoryInformation.getOptionalInformation().getWeight();

        glucoseMandatory = glucoseConverter(glucoseMandatory,
                mandatoryInformation.getPlaceholders().getGlucosePlaceholder(), "mmol/L");
        insulinMandatory = insulinConverter(insulinMandatory,
                mandatoryInformation.getPlaceholders().getInsulinPlaceholder(), "μIU/mL");

        return TEN_EIGHT / (glucoseMandatory.getGlucoseOneTwenty() * insulinMandatory.getInsulinOneTwenty() * vd);
    }
}
