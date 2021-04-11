package com.insulin.formula;

import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;

import static com.insulin.utils.model.MandatoryInfoBuildUtils.*;

/**
 * Static class used extensively for conversion from
 * one unit of measurement to another one.
 */
public final class ValueConverter {

    private ValueConverter() {

    }

    public static GlucoseMandatory glucoseConverter
            (GlucoseMandatory glucoseOriginal, String from, String to) {
        if (from.equals(to)) {
            return glucoseOriginal;
        }
        GlucoseMandatory glucoseMandatory = buildGlucose(glucoseOriginal);
        glucoseMandatory.convert(from);
        return glucoseMandatory;
    }

    public static InsulinMandatory insulinConverter
            (InsulinMandatory insulinOriginal, String from, String to) {
        if (from.equals(to)) {
            return insulinOriginal;
        }
        InsulinMandatory insulinMandatory = buildInsulin(insulinOriginal);
        insulinMandatory.convert(from);
        return insulinMandatory;
    }

    public static double convertSingleGlucose
            (double value, String from, String to) {
        if (from.equals(to)) {
            return value;
        }
        if ("mg/dL".equals(to)) {
            return value * 18;
        }
        return value / 18;
    }

    public static double convertSingleInsulin
            (double value, String from, String to) {
        if (from.equals(to)) {
            return value;
        }
        if ("pmol/L".equals(to)) {
            return value * 6;
        }
        return value / 6;
    }

    public static double convertNefa(double nefa, String from) {
        if (from.equals("mmol/L")) {
            return nefa;
        }
        return nefa * 0.35;
    }

    public static double glucoseMean(GlucoseMandatory glucoseMandatory) {
        return (glucoseMandatory.getFastingGlucose() + glucoseMandatory.getGlucoseThree()
                + glucoseMandatory.getGlucoseSix() + glucoseMandatory.getGlucoseOneTwenty()) / 4;
    }

    public static double insulinMean(InsulinMandatory insulinMandatory) {
        return (insulinMandatory.getFastingInsulin() + insulinMandatory.getInsulinThree()
                + insulinMandatory.getInsulinSix() + insulinMandatory.getInsulinOneTwenty()) / 4;
    }
}
