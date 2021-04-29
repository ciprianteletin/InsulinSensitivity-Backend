package com.insulin.formula.index;

import com.insulin.excel.utils.FormulaExcelUtils;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.ValueConverter.*;
import static com.insulin.formula.ValueConverter.convertSingleInsulin;
import static com.insulin.shared.constants.IndexDataConstants.*;
import static com.insulin.shared.constants.NumericConstants.TEN_EIGHT;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.validation.FormulaValidation.validateWeight;

public class AvingonSih implements FormulaMarker {
    @Override
    public IndexResult calculate(MandatoryInsulinInformation mandatoryInformation) {
        validateWeight(mandatoryInformation.getOptionalInformation(), "Avingon");
        double vd = 150. / mandatoryInformation.getOptionalInformation().getWeight();
        double glucoseOneTwenty = convertSingleGlucose(
                mandatoryInformation.getGlucoseMandatory().getGlucoseOneTwenty(),
                mandatoryInformation.getGlucoseMandatory().getGlucosePlaceholder(),
                "mmol/L");
        double insulinOneTwenty = convertSingleInsulin(
                mandatoryInformation.getInsulinMandatory().getInsulinOneTwenty(),
                mandatoryInformation.getInsulinMandatory().getInsulinPlaceholder(),
                "μIU/mL");

        return buildIndexResult(TEN_EIGHT / (glucoseOneTwenty * insulinOneTwenty * vd));
    }

    @Override
    public String generateExcelFormula(int infoId) {
        StringBuilder formulaBuilder = new StringBuilder();

        String glucoseOneTwentyFormula = FormulaExcelUtils.getGlucose(infoId, "mmol/L", GLUCOSE_ONE_TWENTY);
        String insulinOneTwentyFormula = FormulaExcelUtils.getInsulin(infoId, "μIU/mL", INSULIN_ONE_TWENTY);
        String weightFormula = FormulaExcelUtils.getOptionalNoPlaceholder(infoId, WEIGHT);

        formulaBuilder.append("100000000 /").append("(").append(glucoseOneTwentyFormula)
                .append(" * ").append(insulinOneTwentyFormula).append(" * 150").append("/")
                .append(weightFormula).append(")");
        return formulaBuilder.toString();
    }
}
