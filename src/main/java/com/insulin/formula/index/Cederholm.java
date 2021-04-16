package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;
import org.springframework.data.util.Pair;

import static com.insulin.formula.RangeChecker.checkInBetween;
import static com.insulin.formula.ValueConverter.*;
import static com.insulin.utils.FormulaUtils.glucoseMean;
import static com.insulin.utils.FormulaUtils.insulinMean;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.utils.IndexUtils.healthyPair;
import static com.insulin.validation.FormulaValidation.validateWeight;
import static java.lang.Math.log;

public class Cederholm implements CalculateIndex, IndexInterpreter {
    private final int mean = 79;
    private final int fluctuation = 14;

    @Override
    public IndexResult calculate(MandatoryInsulinInformation mandatoryInformation) {
        validateWeight(mandatoryInformation.getOptionalInformation(), "cederholm");
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();

        glucoseMandatory = glucoseConverter(glucoseMandatory,
                mandatoryInformation.getPlaceholders(), "mmol/L");
        insulinMandatory = insulinConverter(insulinMandatory,
                mandatoryInformation.getPlaceholders(), "μIU/mL");

        double meanGlucose = glucoseMean(glucoseMandatory);
        double meanInsulin = insulinMean(insulinMandatory);
        double result = (75000 + (glucoseMandatory.getFastingGlucose() - glucoseMandatory.getGlucoseOneTwenty())
                * 1.15 * 180 * 0.19 * mandatoryInformation.getOptionalInformation().getWeight())
                / (120 * meanGlucose * log(meanInsulin));

        return buildIndexResult(result, interpret(result), getInterval());
    }

    @Override
    public Pair<String, Severity> interpret(double result) {
        int lowerBound = mean - fluctuation;
        int upperBound = mean + fluctuation;
        if (checkInBetween(lowerBound, upperBound, result)) {
            return healthyPair();
        }
        lowerBound -= fluctuation;
        upperBound -= fluctuation;
        if (checkInBetween(lowerBound, upperBound, result)) {
            return Pair.of("Signs of prediabetes", Severity.PREDIABETES);
        }
        return Pair.of("Type two diabetes", Severity.DIABETES);
    }

    @Override
    public String getInterval() {
        return mean + PLUS_MINUS + fluctuation;
    }
}
