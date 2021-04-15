package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.ValueConverter.*;
import static com.insulin.utils.FormulaUtils.glucoseMean;
import static com.insulin.utils.FormulaUtils.insulinMean;
import static com.insulin.validation.FormulaValidation.validateWeight;
import static java.lang.Math.log;

public class Cederholm implements CalculateIndex, IndexInterpreter {
    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        validateWeight(mandatoryInformation.getOptionalInformation(), "cederholm");
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();

        glucoseMandatory = glucoseConverter(glucoseMandatory,
                mandatoryInformation.getPlaceholders(), "mmol/L");
        insulinMandatory = insulinConverter(insulinMandatory,
                mandatoryInformation.getPlaceholders(), "μIU/mL");

        double meanGlucose = glucoseMean(glucoseMandatory);
        double meanInsulin = insulinMean(insulinMandatory);
        return (75000 + (glucoseMandatory.getFastingGlucose() - glucoseMandatory.getGlucoseOneTwenty())
                * 1.15 * 180 * 0.19 * mandatoryInformation.getOptionalInformation().getWeight())
                / (120 * meanGlucose * log(meanInsulin));
    }

    @Override
    public String interpret(double result) {
        return null;
    }

    @Override
    public String getInterval() {
        return null;
    }
}
