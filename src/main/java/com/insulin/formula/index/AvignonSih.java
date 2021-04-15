package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.shared.constants.NumericConstants.TEN_EIGHT;
import static com.insulin.formula.ValueConverter.glucoseConverter;
import static com.insulin.formula.ValueConverter.insulinConverter;

public class AvignonSih implements CalculateIndex {
    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();
        double vd = 150. / mandatoryInformation.getOptionalInformation().getWeight();

        glucoseMandatory = glucoseConverter(glucoseMandatory,
                mandatoryInformation.getPlaceholders(), "mmol/L");
        insulinMandatory = insulinConverter(insulinMandatory,
                mandatoryInformation.getPlaceholders(), "μIU/mL");

        return TEN_EIGHT / (glucoseMandatory.getGlucoseOneTwenty() * insulinMandatory.getInsulinOneTwenty() * vd);
    }
}
