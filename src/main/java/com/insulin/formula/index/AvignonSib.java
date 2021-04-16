package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.utils.IndexUtils;

import static com.insulin.shared.constants.NumericConstants.TEN_EIGHT;
import static com.insulin.formula.ValueConverter.glucoseConverter;
import static com.insulin.formula.ValueConverter.insulinConverter;
import static com.insulin.utils.IndexUtils.*;

public class AvignonSib implements CalculateIndex {
    @Override
    public IndexResult calculate(MandatoryInsulinInformation mandatoryInformation) {
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();
        int kilograms = mandatoryInformation.getOptionalInformation().getWeight();
        double vd = 150. / kilograms;

        glucoseMandatory = glucoseConverter(glucoseMandatory,
                mandatoryInformation.getPlaceholders(), "mmol/L");
        insulinMandatory = insulinConverter(insulinMandatory,
                mandatoryInformation.getPlaceholders(), "μIU/mL");

        return buildIndexResult(TEN_EIGHT / (glucoseMandatory.getFastingGlucose() * insulinMandatory.getFastingInsulin() * vd));
    }
}
