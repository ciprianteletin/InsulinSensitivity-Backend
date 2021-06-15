package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.excel.utils.FormulaExcelUtils;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryInsulinInformation;
import org.springframework.data.util.Pair;

import static com.insulin.formula.RangeChecker.checkUpperBound;
import static com.insulin.formula.ValueConverter.*;
import static com.insulin.shared.constants.IndexDataConstants.FASTING_INSULIN;
import static com.insulin.shared.constants.IndexDataConstants.TRIGLYCERIDE;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.utils.IndexUtils.healthyPair;
import static com.insulin.validation.FormulaValidation.validateTrygliceride;
import static java.lang.Math.log;

public class McAuley implements FormulaMarker, IndexInterpreter {
    private static final double upperValue = 5.8;

    @Override
    public IndexResult calculate(MandatoryInsulinInformation mandatoryInformation) {
        validateTrygliceride(mandatoryInformation.getOptionalInformation(), "mcAuley");
        double fastingInsulin = mandatoryInformation
                .getInsulinMandatory()
                .getFastingInsulin();
        double trygliceride = mandatoryInformation
                .getOptionalInformation()
                .getTriglyceride();
        fastingInsulin = convertSingleInsulin(fastingInsulin,
                mandatoryInformation.getInsulinMandatory().getInsulinPlaceholder(), "μIU/mL");
        trygliceride = convertTriglycerideMmol(trygliceride,
                mandatoryInformation.getGlucoseMandatory().getGlucosePlaceholder());
        double power = 2.63 - 0.28 * log(fastingInsulin) - 0.31 * log(trygliceride);
        double result = Math.exp(power);

        return buildIndexResult(result, interpret(result), getInterval());
    }

    @Override
    public Pair<String, Severity> interpret(double result) {
        if (checkUpperBound(upperValue, result)) {
            return healthyPair();
        }
        return Pair.of("Insulin Resistance", Severity.INSULIN_RESISTANCE);
    }

    @Override
    public String getInterval() {
        return LESS_THAN + upperValue;
    }

    @Override
    public String generateExcelFormula(int infoId) {
        String fastingInsulin = FormulaExcelUtils.getInsulin(infoId, "μIU/mL", FASTING_INSULIN);
        String trygliceride = FormulaExcelUtils.getOptionalWithPlaceholder(infoId, "mmol/L", TRIGLYCERIDE);

        String logFastingInsulin = FormulaExcelUtils.getLog(fastingInsulin);
        String logTrygliceride = FormulaExcelUtils.getLog(trygliceride);
        String pow = "2.63 - 0.28 * " + logFastingInsulin + " - 0.31 * " + logTrygliceride;
        return FormulaExcelUtils.getExp(pow);
    }
}
