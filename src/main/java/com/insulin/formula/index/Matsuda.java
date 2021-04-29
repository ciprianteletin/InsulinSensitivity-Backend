package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.excel.utils.FormulaExcelUtils;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;
import org.springframework.data.util.Pair;

import static com.insulin.formula.RangeChecker.checkUpperBound;
import static com.insulin.shared.constants.IndexDataConstants.*;
import static com.insulin.shared.constants.NumericConstants.TEN_FOUR;
import static com.insulin.formula.ValueConverter.*;
import static com.insulin.utils.FormulaUtils.glucoseMean;
import static com.insulin.utils.FormulaUtils.insulinMean;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.utils.IndexUtils.healthyPair;

public class Matsuda implements FormulaMarker, IndexInterpreter {
    private static final double interpretValue = 4.3;

    @Override
    public IndexResult calculate(MandatoryInsulinInformation mandatoryInformation) {
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();

        glucoseMandatory = glucoseConverter(glucoseMandatory, "mg/dL");
        insulinMandatory = insulinConverter(insulinMandatory, "μIU/mL");

        double meanGlucose = glucoseMean(glucoseMandatory);
        double meanInsulin = insulinMean(insulinMandatory);

        double result = TEN_FOUR / (Math.sqrt(glucoseMandatory.getFastingGlucose()) * insulinMandatory.getFastingInsulin()
                * meanGlucose * meanInsulin);
        return buildIndexResult(result, interpret(result), getInterval());
    }

    @Override
    public Pair<String, Severity> interpret(double result) {
        if (checkUpperBound(interpretValue, result)) {
            return Pair.of("Insulin Resistance", Severity.INSULIN_RESISTANCE);
        }
        return healthyPair();
    }

    @Override
    public String getInterval() {
        return LESS_THAN + interpretValue;
    }

    @Override
    public String generateExcelFormula(int infoId) {
        StringBuilder formulaBuilder = new StringBuilder();
        String fastingGlucoseFormula = FormulaExcelUtils.getGlucose(infoId, "mg/dL", FASTING_GLUCOSE);
        String fastingInsulin = FormulaExcelUtils.getInsulin(infoId, "μIU/mL", FASTING_INSULIN);
        String sqrtFastingGlucose = FormulaExcelUtils.getSqrt(fastingGlucoseFormula);
        String meanGlucoseFormula = FormulaExcelUtils.getGlucoseMeanIncomplete(infoId, "mg/dL");
        String meanInsulinFormula = FormulaExcelUtils.getInsulinMeanIncomplete(infoId, "μIU/mL");

        formulaBuilder.append("10000 / (").append(sqrtFastingGlucose).append(" * ")
                .append(fastingInsulin).append(" * ").append(meanGlucoseFormula)
                .append(" * ").append(meanInsulinFormula).append(")");

        return formulaBuilder.toString();
    }
}
