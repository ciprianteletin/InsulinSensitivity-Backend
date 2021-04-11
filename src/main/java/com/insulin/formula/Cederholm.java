package com.insulin.formula;

import com.insulin.functional.CalculateIndex;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.ValueConverter.*;
import static java.lang.Math.log;

public class Cederholm implements CalculateIndex {
    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();

        glucoseMandatory = glucoseConverter(glucoseMandatory,
                mandatoryInformation.getPlaceholders().getGlucosePlaceholder(), "mmol/L");
        insulinMandatory = insulinConverter(insulinMandatory,
                mandatoryInformation.getPlaceholders().getInsulinPlaceholder(), "μIU/mL");

        double meanGlucose = (glucoseMandatory.getFastingGlucose() + glucoseMandatory.getGlucoseThree()
                + glucoseMandatory.getGlucoseSix() + glucoseMandatory.getGlucoseOneTwenty()) / 4;
        double meanInsulin = (insulinMandatory.getFastingInsulin() + insulinMandatory.getInsulinThree()
                + insulinMandatory.getInsulinSix() + insulinMandatory.getInsulinOneTwenty()) / 4;
        return (75000 +
                (glucoseMandatory.getFastingGlucose() - glucoseMandatory.getGlucoseOneTwenty())
                        * 1.15 * 180 * 0.19 * mandatoryInformation.getOptionalInformation().getWeight()
        ) / (120 * meanGlucose * log(meanInsulin));
    }
}
