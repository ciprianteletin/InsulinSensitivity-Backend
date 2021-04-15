package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.RangeChecker.checkInBetween;
import static com.insulin.formula.ValueConverter.*;
import static com.insulin.utils.FormulaUtils.glucoseMean;
import static com.insulin.utils.FormulaUtils.insulinMean;
import static com.insulin.validation.FormulaValidation.validateWeight;
import static java.lang.Math.log;

public class Gutt implements CalculateIndex, IndexInterpreter {
    private final int mean = 89;
    private final int fluctuation = 39;

    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        validateWeight(mandatoryInformation.getOptionalInformation(), "gutt");
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();


        GlucoseMandatory copyGlucose = glucoseConverter(glucoseMandatory,
                mandatoryInformation.getPlaceholders(), "mg/dL");
        insulinMandatory = insulinConverter(insulinMandatory,
                mandatoryInformation.getPlaceholders(), "μIU/mL");
        glucoseMandatory = glucoseConverter(glucoseMandatory,
                mandatoryInformation.getPlaceholders(), "mmol/L");

        double meanGlucose = glucoseMean(glucoseMandatory);
        double meanInsulin = insulinMean(insulinMandatory);

        return (75000 + (copyGlucose.getFastingGlucose() - copyGlucose.getGlucoseOneTwenty())
                * 0.19 * mandatoryInformation.getOptionalInformation().getWeight()
        ) / (120 * meanGlucose * log(meanInsulin));
    }

    @Override
    public String interpret(double result) {
        int lowerBound = mean - fluctuation;
        int upperBound = mean + fluctuation;

        if (checkInBetween(lowerBound, upperBound, result)) {
            return "Healthy";
        }
        lowerBound = 35;
        upperBound = 82;
        if (checkInBetween(lowerBound, upperBound, result)) {
            return "Prediabetes with high chance of diabetes";
        }
        return "Insulin resistance, diabetes";
    }

    @Override
    public String getInterval() {
        return mean + PLUS_MINUS + fluctuation;
    }
}
