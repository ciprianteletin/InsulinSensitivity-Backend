package com.insulin.utils;

import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.OptionalIndexInformation;

import static java.lang.Math.pow;

/**
 * Class which offers a variety of formulas that are used in the process of index calculation.
 * Instead of calculating directly in the specific index class, a dedicated class was created
 * to avoid code duplicate.
 */
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

    public static double calculateBMI(OptionalIndexInformation optionalInformation) {
        double weight = optionalInformation.getWeight();
        double height = optionalInformation.getHeight() / 100;

        return weight / pow(height, 2);
    }
}
