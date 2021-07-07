package com.insulin.formula;

import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.OptionalIndexInformation;

import static com.insulin.utils.MandatoryInfoBuildUtils.*;

/**
 * Static class used extensively for conversion from
 * one unit of measurement to another one.
 */
public final class ValueConverter {
    public static final int GLUCOSE_CONVERT = 18;
    public static final int INSULIN_CONVERT = 6;
    public static final String PLUS_MINUS = "±";
    public static final String LESS_THAN = "<";
    public static final String APPROXIMATE = "≅";
    public static final String GREATER_THAN = ">";
    public static final String DASH = "-";
    public static final double NEFA_CONVERT = 0.35;
    public static final double TRIGLYCERIDE_CONVERT = 0.01129;
    public static final double CHOL_CONVERT = 38.67;

    private ValueConverter() {

    }

    public static GlucoseMandatory glucoseConverter(GlucoseMandatory glucoseOriginal, String to) {
        String from = glucoseOriginal.getGlucosePlaceholder();
        if (from.equals(to)) {
            return glucoseOriginal;
        }
        GlucoseMandatory glucoseMandatory = buildGlucose(glucoseOriginal);
        glucoseMandatory.convert();
        return glucoseMandatory;
    }

    public static InsulinMandatory insulinConverter(InsulinMandatory insulinOriginal, String to) {
        String from = insulinOriginal.getInsulinPlaceholder();
        if (from.equals(to)) {
            return insulinOriginal;
        }
        InsulinMandatory insulinMandatory = buildInsulin(insulinOriginal);
        insulinMandatory.convert();
        return insulinMandatory;
    }

    public static OptionalIndexInformation optionalConverter(OptionalIndexInformation optionalInsulin, String from, String to) {
        if (from.equals(to)) {
            return optionalInsulin;
        }
        OptionalIndexInformation copyOptional = buildOptional(optionalInsulin);
        copyOptional.convert(from);
        return copyOptional;
    }

    public static double convertSingleGlucose(double value, String from, String to) {
        if (from.equals(to)) {
            return value;
        }
        if ("mmol/L".equals(to)) {
            return value / GLUCOSE_CONVERT;
        }
        return value * GLUCOSE_CONVERT;
    }

    public static double convertSingleInsulin(double value, String from, String to) {
        if (from.equals(to)) {
            return value;
        }
        if ("pmol/L".equals(to)) {
            return value * INSULIN_CONVERT;
        }
        return value / INSULIN_CONVERT;
    }

    public static double convertNefaMmol(double nefa, String from) {
        if (from.equals("mmol/L")) {
            return nefa;
        }
        return nefa * NEFA_CONVERT;
    }

    public static double convertTriglycerideMmol(double value, String from) {
        if (from.equals("mmol/L")) {
            return value;
        }
        return value * TRIGLYCERIDE_CONVERT;
    }

    public static double convertTriglycerideMgdl(double value, String from) {
        if (from.equals("mg/dL")) {
            return value;
        }
        return value / TRIGLYCERIDE_CONVERT;
    }

    public static double convertCholesterolMmol(double value, String from) {
        if (from.equals("mmol/L")) {
            return value;
        }
        return value / CHOL_CONVERT;
    }

    public static double convertCholesterolMgdl(double value, String from) {
        if (from.equals("mg/dL")) {
            return value;
        }
        return value * CHOL_CONVERT;
    }
}
