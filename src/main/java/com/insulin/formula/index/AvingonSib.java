package com.insulin.formula.index;

import com.insulin.excel.utils.FormulaExcelUtils;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryIndexInformation;

import static com.insulin.formula.ValueConverter.*;
import static com.insulin.formula.ValueConverter.convertSingleInsulin;
import static com.insulin.shared.constants.IndexDataConstants.*;
import static com.insulin.shared.constants.NumericConstants.TEN_EIGHT;
import static com.insulin.utils.IndexUtils.*;
import static com.insulin.validation.FormulaValidation.validateWeight;

public class AvingonSib implements FormulaMarker {
    @Override
    public IndexResult calculate(MandatoryIndexInformation mandatoryInformation) {
        validateWeight(mandatoryInformation.getOptionalInformation(), "Avingon");
        double fastingGlucose = convertSingleGlucose(
                mandatoryInformation.getGlucoseMandatory().getFastingGlucose(),
                mandatoryInformation.getGlucoseMandatory().getGlucosePlaceholder(),
                "mmol/L");
        double fastingInsulin = convertSingleInsulin(
                mandatoryInformation.getInsulinMandatory().getFastingInsulin(),
                mandatoryInformation.getInsulinMandatory().getInsulinPlaceholder(),
                "μIU/mL");

        double kilograms = mandatoryInformation.getOptionalInformation().getWeight();
        double vd = 150. / kilograms;

        return buildIndexResult(TEN_EIGHT / (fastingGlucose * fastingInsulin * vd));
    }

    @Override
    public String generateExcelFormula(int infoId) {
        StringBuilder formulaBuilder = new StringBuilder();

        String weightFormula = FormulaExcelUtils.getOptionalNoPlaceholder(infoId, WEIGHT);
        String fastingGlucose = FormulaExcelUtils.getGlucose(infoId, "mmol/L", FASTING_GLUCOSE);
        String fastingInsulin = FormulaExcelUtils.getInsulin(infoId, "μIU/mL", FASTING_INSULIN);

        formulaBuilder.append("100000000 /").append("(").append(fastingGlucose)
                .append(" * ").append(fastingInsulin).append(" * 150").append("/")
                .append(weightFormula).append(")");
        return formulaBuilder.toString();
    }
}
