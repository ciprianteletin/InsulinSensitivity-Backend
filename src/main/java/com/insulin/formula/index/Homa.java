package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryInsulinInformation;
import org.springframework.data.util.Pair;

import static com.insulin.formula.RangeChecker.checkInBetween;
import static com.insulin.formula.RangeChecker.checkLowerBound;
import static com.insulin.formula.ValueConverter.*;
import static com.insulin.utils.IndexUtils.*;

public class Homa implements CalculateIndex, IndexInterpreter {
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

        double result = (fastingGlucose * fastingInsulin) / 22.5;
        return buildIndexResult(result, interpret(result), getInterval());
    }

    @Override
    public Pair<String, Severity> interpret(double result) {
        if (checkInBetween(0.5, 1.4, result)) {
            return healthyPair();
        }
        if (checkInBetween(1.9, 2.9, result)) {
            return Pair.of("Early Insulin Resistance", Severity.INSULIN_RESISTANCE);
        }
        if (checkLowerBound(2.9, result)) {
            return Pair.of("Insulin Resistance", Severity.INSULIN_RESISTANCE);
        }
        return defaultPair();
    }

    @Override
    public String getInterval() {
        return "0.5" + DASH + "1.4";
    }
}
