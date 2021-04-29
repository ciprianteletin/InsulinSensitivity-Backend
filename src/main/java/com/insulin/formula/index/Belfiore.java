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

import static com.insulin.formula.RangeChecker.checkLowerBound;
import static com.insulin.formula.RangeChecker.checkUpperBound;
import static com.insulin.formula.ValueConverter.*;
import static com.insulin.shared.constants.NumericConstants.*;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.utils.IndexUtils.healthyPair;

public class Belfiore implements FormulaMarker, IndexInterpreter {
    @Override
    public IndexResult calculate(MandatoryInsulinInformation mandatoryInformation) {
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();

        glucoseMandatory = glucoseConverter(glucoseMandatory, "mmol/L");
        insulinMandatory = insulinConverter(insulinMandatory, "μIU/mL");

        double glucoseSubject = 0.5 * glucoseMandatory.getFastingGlucose() + glucoseMandatory.getGlucoseSix()
                + glucoseMandatory.getGlucoseOneTwenty();
        double insulinSubject = 0.5 * insulinMandatory.getFastingInsulin() + insulinMandatory.getInsulinSix()
                + insulinMandatory.getInsulinOneTwenty();

        double glucoseNormal = 0.5 * FASTING_GLUCOSE_MM + GLUCOSE_SIX_MM + GLUCOSE_ONE_TWENTY_MM;
        double insulinNormal = 0.5 * FASTING_INSULIN_UI + INSULIN_SIX_UI + INSULIN_ONE_TWENTY_UI;

        double result = 2 / ((glucoseSubject / glucoseNormal) * (insulinSubject / insulinNormal) + 1);
        return buildIndexResult(result, interpret(result), getInterval());
    }

    @Override
    public Pair<String, Severity> interpret(double result) {
        double errorValue = 0.27;
        double lowerBound = 1 + errorValue;
        double upperBound = 1 - errorValue;
        if (checkLowerBound(lowerBound, result)) {
            return Pair.of("Insulin resistance", Severity.INSULIN_RESISTANCE);
        }
        if (checkUpperBound(upperBound, result)) {
            return Pair.of("Type 2 diabetes", Severity.DIABETES);
        }
        return healthyPair();
    }

    @Override
    public String getInterval() {
        return APPROXIMATE + "1";
    }

    @Override
    public String generateExcelFormula(int infoId) {
        StringBuilder formulaBuilder = new StringBuilder();
        String glucoseNormal = FormulaExcelUtils.getGlucoseNormal();
        String insulinNormal = FormulaExcelUtils.getInsulinNormal();
        String glucoseSubject = FormulaExcelUtils.getGlucoseSubject(infoId, "mmol/L");
        String insulinSubject = FormulaExcelUtils.getInsulinSubject(infoId, "μIU/mL");

        formulaBuilder.append("2 / ((").append(glucoseSubject).append(" / ").append(glucoseNormal)
                .append(") * (").append(insulinSubject).append(" / ").append(insulinNormal)
                .append(") + 1 )");
        return formulaBuilder.toString();
    }
}
