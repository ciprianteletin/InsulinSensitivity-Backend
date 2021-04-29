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
import static com.insulin.shared.constants.IndexDataConstants.*;
import static com.insulin.utils.IndexUtils.*;
import static com.insulin.validation.FormulaValidation.validateNefa;
import static java.lang.Math.log;

public class RevisedQuicki implements FormulaMarker, IndexInterpreter {
    private final double interpretValue = 0.448;
    private final double fluctuation = 0.013;

    @Override
    public IndexResult calculate(MandatoryInsulinInformation mandatoryInformation) {
        validateNefa(mandatoryInformation.getOptionalInformation(), "revised quicki");
        double fastingGlucose = convertSingleGlucose(
                mandatoryInformation.getGlucoseMandatory().getFastingGlucose(),
                mandatoryInformation.getGlucoseMandatory().getGlucosePlaceholder(),
                "mmol/L");
        double fastingInsulin = convertSingleInsulin(
                mandatoryInformation.getInsulinMandatory().getFastingInsulin(),
                mandatoryInformation.getInsulinMandatory().getInsulinPlaceholder(),
                "μIU/mL");
        double nefa = convertNefa(mandatoryInformation.getOptionalInformation().getNefa(),
                mandatoryInformation.getGlucoseMandatory().getGlucosePlaceholder());

        double result = 1.0 / (log(fastingGlucose) + log(fastingInsulin) + log(nefa));
        return buildIndexResult(result, interpret(result), getInterval());
    }

    @Override
    public Pair<String, Severity> interpret(double result) {
        double upperBound = interpretValue + fluctuation;
        double lowerBound = interpretValue - fluctuation;
        if (checkInBetween(lowerBound, upperBound, result)) {
            return healthyPair();
        }
        upperBound = 0.367 + 0.008;
        lowerBound = 0.367 - 0.008;
        if (checkInBetween(lowerBound, upperBound, result)) {
            return Pair.of("Prediabetes", Severity.PREDIABETES);
        }
        upperBound = 0.323 + 0.007;
        lowerBound = 0.323 - 0.007;
        if (checkInBetween(lowerBound, upperBound, result)) {
            return Pair.of("Type 2 diabetes", Severity.DIABETES);
        }
        return defaultPair();
    }

    @Override
    public String getInterval() {
        return interpretValue + PLUS_MINUS + fluctuation;
    }

    @Override
    public String generateExcelFormula(int infoId) {
        StringBuilder formulaBuilder = new StringBuilder();

        String fastingGlucoseFormula = FormulaExcelUtils.getGlucose(infoId, "mmol/L", FASTING_GLUCOSE);
        String fastingInsulinFormula = FormulaExcelUtils.getInsulin(infoId, "μIU/mL", FASTING_INSULIN);
        String nefa = FormulaExcelUtils.getOptionalWithPlaceholder(infoId, "mmol/L", NEFA);

        formulaBuilder.append("1.0 / (").append(FormulaExcelUtils.getLog(fastingGlucoseFormula))
                .append(" + ").append(FormulaExcelUtils.getLog(fastingInsulinFormula))
                .append(" + ")
                .append(FormulaExcelUtils.getLog(nefa)).append(")");
        return formulaBuilder.toString();
    }
}
