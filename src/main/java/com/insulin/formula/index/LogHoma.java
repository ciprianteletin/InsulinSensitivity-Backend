package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.excel.utils.FormulaExcelUtils;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryIndexInformation;
import org.springframework.data.util.Pair;

import static com.insulin.formula.RangeChecker.checkInBetween;
import static com.insulin.formula.ValueConverter.PLUS_MINUS;
import static com.insulin.shared.constants.IndexDataConstants.FASTING_GLUCOSE;
import static com.insulin.shared.constants.IndexDataConstants.FASTING_INSULIN;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.utils.IndexUtils.healthyPair;
import static java.lang.Math.log;

public class LogHoma implements FormulaMarker, IndexInterpreter {
    private final static double interpretValue = 1.0;
    private final static double fluctuation = 0.64;

    @Override
    public IndexResult calculate(MandatoryIndexInformation mandatoryInformation) {
        Homa homa = new Homa();
        double result = log(homa.calculate(mandatoryInformation).getResult());
        return buildIndexResult(result, interpret(result), getInterval());
    }

    @Override
    public Pair<String, Severity> interpret(double result) {
        double lowerBound = interpretValue - fluctuation;
        double upperBound = interpretValue + fluctuation;
        if (checkInBetween(lowerBound, upperBound, result)) {
            return healthyPair();
        }
        return Pair.of("Prediabetes", Severity.PREDIABETES);
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

        formulaBuilder.append("(").append(fastingGlucoseFormula).append(" * ")
                .append(fastingInsulinFormula).append(")").append("/ 22.5");

        return FormulaExcelUtils.getLog(formulaBuilder.toString());
    }
}
