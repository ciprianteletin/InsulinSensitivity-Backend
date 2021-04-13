package com.insulin.formula;

import com.insulin.functional.CalculateIndex;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.model.form.OptionalInsulinInformation;

import static com.insulin.formula.NumericConstants.TEN_FOUR;
import static com.insulin.formula.ValueConverter.glucoseConverter;
import static com.insulin.formula.ValueConverter.insulinConverter;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Ogis implements CalculateIndex {
    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();

        glucoseMandatory = glucoseConverter(glucoseMandatory,
                mandatoryInformation.getPlaceholders().getGlucosePlaceholder(), "mmol/L");
        insulinMandatory = insulinConverter(insulinMandatory,
                mandatoryInformation.getPlaceholders().getInsulinPlaceholder(), "pmol/L");

        double glucoseNine = (glucoseMandatory.getGlucoseOneTwenty() + glucoseMandatory.getGlucoseSix()) / 2;
        double insulinNine = (insulinMandatory.getInsulinOneTwenty() + insulinMandatory.getInsulinSix()) / 2;

        double pp1 = 6.5, pp2 = 1951, pp3 = 4514;
        double pp4 = 792, pp5 = 0.0118, pp6 = 173;
        double dt = 30, glc = 90 * 0.05551;
        OptionalInsulinInformation optionalInformation = mandatoryInformation.getOptionalInformation();
        double bsa = 0.1640443958298 * pow(optionalInformation.getWeight(), 0.515)
                * pow((0.01 * optionalInformation.getHeight()), 0.422);
        double ndose = 5.551 * 75 / bsa;

        double temp1 = pp4 * ((pp1 * ndose - TEN_FOUR * (glucoseMandatory.getGlucoseOneTwenty() - glucoseNine) / dt) /
                glucoseNine + pp3 / glucoseMandatory.getFastingGlucose()) /
                (insulinNine - insulinMandatory.getFastingInsulin() + pp2);
        double temp2 = (pp5 * (glucoseNine - glc) + 1) * temp1;

        return (temp2 + sqrt(pow(temp2, 2) + 4 * pp5 * pp6 * (glucoseNine - glc) * temp1)) / 2;
    }
}
