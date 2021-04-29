package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.excel.utils.FormulaExcelUtils;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryInsulinInformation;
import org.springframework.data.util.Pair;

import static com.insulin.formula.RangeChecker.checkInBetween;
import static com.insulin.formula.RangeChecker.checkLowerBound;
import static com.insulin.formula.ValueConverter.*;
import static com.insulin.shared.constants.IndexDataConstants.FASTING_GLUCOSE;
import static com.insulin.shared.constants.IndexDataConstants.FASTING_INSULIN;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.utils.IndexUtils.healthyPair;
import static java.lang.Math.log;

public class Quicki implements FormulaMarker, IndexInterpreter {
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

        double result = 1.0 / (log(fastingGlucose) + log(fastingInsulin));
        return buildIndexResult(result, interpret(result), getInterval());
    }

    @Override
    public Pair<String, Severity> interpret(double result) {
        if (checkLowerBound(0.45, result)) {
            return healthyPair();
        }
        if (checkInBetween(0.30, 0.45, result)) {
            return Pair.of("Insulin Resistance", Severity.INSULIN_RESISTANCE);
        }
        return Pair.of("Diabetes", Severity.DIABETES);
    }

    @Override
    public String getInterval() {
        return GREATER_THAN + 0.45;
    }

    @Override
    public String generateExcelFormula(int infoId) {
        StringBuilder formulaBuilder = new StringBuilder();

        String fastingGlucoseFormula = FormulaExcelUtils.getGlucose(infoId, "mmol/L", FASTING_GLUCOSE);
        String fastingInsulinFormula = FormulaExcelUtils.getInsulin(infoId, "μIU/mL", FASTING_INSULIN);

        formulaBuilder.append("1.0 / (").append(FormulaExcelUtils.getLog(fastingGlucoseFormula))
                .append(" + ").append(FormulaExcelUtils.getLog(fastingInsulinFormula)).append(")");
        return formulaBuilder.toString();
    }
}
