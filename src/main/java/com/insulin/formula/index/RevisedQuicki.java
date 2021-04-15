package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.RangeChecker.checkInBetween;
import static com.insulin.formula.ValueConverter.*;
import static com.insulin.validation.FormulaValidation.validateNefa;
import static java.lang.Math.log;

public class RevisedQuicki implements CalculateIndex, IndexInterpreter {
    private final double interpretValue = 0.448;
    private final double fluctuation = 0.013;

    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        validateNefa(mandatoryInformation.getOptionalInformation(), "revised quicki");
        double fastingGlucose = convertSingleGlucose(
                mandatoryInformation.getGlucoseMandatory().getFastingGlucose(),
                mandatoryInformation.getPlaceholders().getGlucosePlaceholder(),
                "mmol/L");
        double fastingInsulin = convertSingleInsulin(
                mandatoryInformation.getInsulinMandatory().getFastingInsulin(),
                mandatoryInformation.getPlaceholders().getInsulinPlaceholder(),
                "μIU/mL");
        double nefa = convertNefa(mandatoryInformation.getOptionalInformation().getNefa(),
                mandatoryInformation.getPlaceholders().getGlucosePlaceholder());

        return 1.0 / (log(fastingGlucose) + log(fastingInsulin) + log(nefa));
    }

    @Override
    public String interpret(double result) {
        double upperBound = interpretValue + fluctuation;
        double lowerBound = interpretValue - fluctuation;
        if (checkInBetween(lowerBound, upperBound, result)) {
            return "Healthy";
        }
        upperBound = 0.367 + 0.008;
        lowerBound = 0.367 - 0.008;
        if (checkInBetween(lowerBound, upperBound, result)) {
            return "Prediabetes";
        }
        upperBound = 0.323 + 0.007;
        lowerBound = 0.323 - 0.007;
        if (checkInBetween(lowerBound, upperBound, result)) {
            return "Type 2 diabetes";
        }
        return "-";
    }

    @Override
    public String getInterval() {
        return interpretValue + PLUS_MINUS + fluctuation;
    }
}
