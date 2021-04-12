package com.insulin.formula;

import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;

import static com.insulin.utils.model.MandatoryInfoBuildUtils.*;

/**
 * Static class used extensively for conversion from
 * one unit of measurement to another one.
 */
public final class ValueConverter {
    public static final int GLUCOSE_CONVERT = 18;
    public static final int INSULIN_CONVERT = 6;
    private static final double NEFA_CONVERT = 0.35;
    private static final double TRYGLICERIDE_CONVERT = 0.01129;

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
            return value * GLUCOSE_CONVERT;
        }
        return value / GLUCOSE_CONVERT;
    }

    public static double convertSingleInsulin
            (double value, String from, String to) {
        if (from.equals(to)) {
            return value;
        }
        if ("pmol/L".equals(to)) {
            return value * INSULIN_CONVERT;
        }
        return value / INSULIN_CONVERT;
    }

    public static double convertNefa(double nefa, String from) {
        if (from.equals("mmol/L")) {
            return nefa;
        }
        return nefa * NEFA_CONVERT;
    }

    public static double convertTrygliceride(double value, String from) {
        if (from.equals("mmol/L")) {
            return value;
        }
        return value * TRYGLICERIDE_CONVERT;
    }
}
