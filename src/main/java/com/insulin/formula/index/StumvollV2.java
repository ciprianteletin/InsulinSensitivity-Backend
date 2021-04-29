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

import static com.insulin.formula.ValueConverter.*;
import static com.insulin.shared.constants.IndexDataConstants.*;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.utils.IndexUtils.defaultPair;

public class StumvollV2 implements FormulaMarker, IndexInterpreter {
    @Override
    public IndexResult calculate(MandatoryInsulinInformation mandatoryInformation) {
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();

        double fastingInsulin = convertSingleInsulin(insulinMandatory.getFastingInsulin(),
                mandatoryInformation.getInsulinMandatory().getInsulinPlaceholder(), "pmol/L");
        double insulin120 = convertSingleInsulin(insulinMandatory.getInsulinOneTwenty(),
                mandatoryInformation.getInsulinMandatory().getInsulinPlaceholder(), "pmol/L");
        double glucose120 = convertSingleGlucose(glucoseMandatory.getGlucoseOneTwenty(),
                mandatoryInformation.getGlucoseMandatory().getGlucosePlaceholder(), "mmol/L");

        double result = 0.156 - 0.0000459 * insulin120 - 0.000321 * fastingInsulin - 0.00541 * glucose120;
        return buildIndexResult(result, interpret(result));
    }

    @Override
    public Pair<String, Severity> interpret(double result) {
        return defaultPair();
    }

    @Override
    public String getInterval() {
        return "-";
    }

    @Override
    public String generateExcelFormula(int infoId) {
        String insulinOneTwenty = FormulaExcelUtils.getInsulin(infoId, "pmol/L", INSULIN_ONE_TWENTY);
        String fastingInsulin = FormulaExcelUtils.getInsulin(infoId, "pmol/L", FASTING_INSULIN);
        String glucoseOneTwenty = FormulaExcelUtils.getGlucose(infoId, "mmol/L", GLUCOSE_ONE_TWENTY);

        return "0.156 - 0.0000459 * " + insulinOneTwenty + " - 0.000321 * " + fastingInsulin + " - 0.00541 * " + glucoseOneTwenty;
    }
}
