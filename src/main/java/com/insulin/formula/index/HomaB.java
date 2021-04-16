package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryInsulinInformation;
import org.springframework.data.util.Pair;

import static com.insulin.formula.RangeChecker.checkInBetween;
import static com.insulin.formula.ValueConverter.*;
import static com.insulin.utils.IndexUtils.*;

public class HomaB implements CalculateIndex, IndexInterpreter {
    private static final double limit = 113.10;
    private static final double fluctuation = 30.56;

    @Override
    public IndexResult calculate(MandatoryInsulinInformation mandatoryInformation) {
        double fastingGlucose = convertSingleGlucose(
                mandatoryInformation.getGlucoseMandatory().getFastingGlucose(),
                mandatoryInformation.getGlucoseMandatory().getGlucosePlaceholder(),
                "mmol/L");
        double fastingInsulin = convertSingleInsulin(
                mandatoryInformation.getInsulinMandatory().getFastingInsulin(),
                mandatoryInformation.getInsulinMandatory().getInsulinPlaceholder(),
                "μIU/mL");

        double result = (20 * fastingInsulin) / (fastingGlucose - 3.5);
        return buildIndexResult(result, interpret(result), getInterval());
    }

    @Override
    public Pair<String, Severity> interpret(double result) {
        double lowerBound = limit - fluctuation;
        double upperBound = limit + fluctuation;

        if (checkInBetween(lowerBound, upperBound, result)) {
            return healthyPair();
        }

        lowerBound = 47.10 - 24.67;
        upperBound = 47.10 + 24.67;
        if (checkInBetween(lowerBound, upperBound, result)) {
            return Pair.of("Diabetes", Severity.DIABETES);
        }
        return defaultPair();
    }

    @Override
    public String getInterval() {
        return limit + PLUS_MINUS + fluctuation;
    }
}
