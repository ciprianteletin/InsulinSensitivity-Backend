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
}
