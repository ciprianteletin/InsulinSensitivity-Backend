package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.RangeChecker.checkLowerBound;
import static com.insulin.formula.RangeChecker.checkUpperBound;
import static com.insulin.formula.ValueConverter.*;
import static com.insulin.shared.constants.NumericConstants.*;

public class Belfiore implements CalculateIndex, IndexInterpreter {
    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();

        glucoseMandatory = glucoseConverter(glucoseMandatory,
                mandatoryInformation.getPlaceholders(), "mmol/L");
        insulinMandatory = insulinConverter(insulinMandatory,
                mandatoryInformation.getPlaceholders(), "μIU/mL");

        double glucoseSubject = 0.5 * glucoseMandatory.getFastingGlucose() + glucoseMandatory.getGlucoseSix()
                + glucoseMandatory.getGlucoseOneTwenty();
        double insulinSubject = 0.5 * insulinMandatory.getFastingInsulin() + insulinMandatory.getInsulinSix()
                + insulinMandatory.getInsulinOneTwenty();

        double glucoseNormal = 0.5 * FASTING_GLUCOSE_MM + GLUCOSE_SIX_MM + GLUCOSE_ONE_TWENTY_MM;
        double insulinNormal = 0.5 * FASTING_INSULIN_UI + INSULIN_SIX_UI + INSULIN_ONE_TWENTY_UI;

        return 2 / ((glucoseSubject / glucoseNormal) * (insulinSubject / insulinNormal) + 1);
    }

    @Override
    public String interpret(double result) {
        double errorValue = 0.27;
        double lowerBound = 1 + errorValue;
        double upperBound = 1 - errorValue;
        if (checkLowerBound(lowerBound, result)) {
            return "Insulin resistance";
        }
        if (checkUpperBound(upperBound, result)) {
            return "Type 2 diabetes";
        }
        return "Healthy";
    }

    @Override
    public String getInterval() {
        return APPROXIMATE + "1";
    }
}
