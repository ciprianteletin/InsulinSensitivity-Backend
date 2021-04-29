package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.excel.utils.FormulaExcelUtils;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryInsulinInformation;
import org.springframework.data.util.Pair;

import static com.insulin.formula.RangeChecker.checkInBetween;
import static com.insulin.formula.ValueConverter.*;
import static com.insulin.shared.constants.IndexDataConstants.FASTING_GLUCOSE;
import static com.insulin.shared.constants.IndexDataConstants.FASTING_INSULIN;
import static com.insulin.utils.IndexUtils.*;

public class HomaB implements FormulaMarker, IndexInterpreter {
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

    @Override
    public String generateExcelFormula(int infoId) {
        StringBuilder formulaBuilder = new StringBuilder();

        String fastingGlucoseFormula = FormulaExcelUtils.getGlucose(infoId, "mmol/L", FASTING_GLUCOSE);
        String fastingInsulinFormula = FormulaExcelUtils.getInsulin(infoId, "μIU/mL", FASTING_INSULIN);

        formulaBuilder.append("(20 * ").append(fastingInsulinFormula).append(")")
                .append(" / (").append(fastingGlucoseFormula).append(" - 3.5)");
        return formulaBuilder.toString();
    }
}
