package com.insulin.utils;

import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.model.form.OptionalInsulinInformation;

import static java.lang.Math.pow;

public final class FormulaUtils {
    private FormulaUtils() {

    }

    public static double glucoseMean(GlucoseMandatory glucoseMandatory) {
        return (glucoseMandatory.getFastingGlucose() + glucoseMandatory.getGlucoseThree()
                + glucoseMandatory.getGlucoseSix() + glucoseMandatory.getGlucoseOneTwenty()) / 4;
    }

    public static double insulinMean(InsulinMandatory insulinMandatory) {
        return (insulinMandatory.getFastingInsulin() + insulinMandatory.getInsulinThree()
                + insulinMandatory.getInsulinSix() + insulinMandatory.getInsulinOneTwenty()) / 4;
    }

    public static double calculateBMI(OptionalInsulinInformation optionalInformation) {
        int weight = optionalInformation.getWeight();
        int height = optionalInformation.getHeight() / 100;

        return weight / pow(height, 2);
    }
}
