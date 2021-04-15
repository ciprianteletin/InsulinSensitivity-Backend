package com.insulin.formula.result;

import com.insulin.interfaces.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

import static com.insulin.formula.RangeChecker.checkLowerBound;
import static com.insulin.formula.RangeChecker.checkUpperBound;

public class DiabetesNormalFasting implements Interpreter {
    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double upperLimitFastingGlucose = 100;
        double lowerLimitPostGlucose2h = 199;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return checkUpperBound(upperLimitFastingGlucose, fastingGlucose) && //
                checkLowerBound(lowerLimitPostGlucose2h, glucoseTwoH);
    }

    @Override
    public String getName() {
        return "Diabetes with Normal Fasting Glucose";
    }
}
