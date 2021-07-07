package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.excel.utils.FormulaExcelUtils;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryIndexInformation;
import org.springframework.data.util.Pair;

import static com.insulin.formula.RangeChecker.checkInBetween;
import static com.insulin.formula.ValueConverter.*;
import static com.insulin.shared.constants.IndexDataConstants.*;
import static com.insulin.utils.FormulaUtils.glucoseMean;
import static com.insulin.utils.FormulaUtils.insulinMean;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.utils.IndexUtils.healthyPair;
import static com.insulin.validation.FormulaValidation.validateWeight;
import static java.lang.Math.log;

public class Gutt implements FormulaMarker, IndexInterpreter {
    private final int mean = 89;
    private final int fluctuation = 39;

    @Override
    public IndexResult calculate(MandatoryIndexInformation mandatoryInformation) {
        validateWeight(mandatoryInformation.getOptionalInformation(), "Gutt");
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();


        GlucoseMandatory copyGlucose = glucoseConverter(glucoseMandatory, "mg/dL");
        insulinMandatory = insulinConverter(insulinMandatory, "μIU/mL");
        glucoseMandatory = glucoseConverter(glucoseMandatory, "mmol/L");

        double meanGlucose = glucoseMean(glucoseMandatory);
        double meanInsulin = insulinMean(insulinMandatory);

        double result = (75000 + (copyGlucose.getFastingGlucose() - copyGlucose.getGlucoseOneTwenty())
                * 0.19 * mandatoryInformation.getOptionalInformation().getWeight())
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
        lowerBound = 35;
        upperBound = 82;
        if (checkInBetween(lowerBound, upperBound, result)) {
            return Pair.of("Prediabetes with high chance of diabetes", Severity.PREDIABETES);
        }
        return Pair.of("Insulin resistance, diabetes", Severity.DIABETES);
    }

    @Override
    public String getInterval() {
        return mean + PLUS_MINUS + fluctuation;
    }

    @Override
    public String generateExcelFormula(int infoId) {
        StringBuilder formulaBuilder = new StringBuilder();
        String fastingGlucoseFormula = FormulaExcelUtils.getGlucose(infoId, "mg/dL", FASTING_GLUCOSE);
        String glucoseOneTwoFormula = FormulaExcelUtils.getGlucose(infoId, "mg/dL", GLUCOSE_ONE_TWENTY);
        String weightFormula = FormulaExcelUtils.getOptionalNoPlaceholder(infoId, WEIGHT);
        String meanGlucoseFormula = FormulaExcelUtils.getGlucoseMeanIncomplete(infoId, "mmol/L");
        String meanInsulinFormula = FormulaExcelUtils.getInsulinMeanIncomplete(infoId, "μIU/mL");
        String logMeanInsulin = FormulaExcelUtils.getLog(meanInsulinFormula);

        formulaBuilder.append("(75000 + (").append(fastingGlucoseFormula).append("-")
                .append(glucoseOneTwoFormula).append(")").append("* 0.19 *")
                .append(weightFormula).append(")").append("/(120 * ").append(meanGlucoseFormula)
                .append("*").append(logMeanInsulin).append(")");

        return formulaBuilder.toString();
    }
}
