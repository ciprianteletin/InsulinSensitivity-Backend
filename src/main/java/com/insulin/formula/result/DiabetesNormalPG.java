package com.insulin.formula.result;

import com.insulin.interfaces.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

import static com.insulin.formula.RangeChecker.checkLowerBound;
import static com.insulin.formula.RangeChecker.checkUpperBound;

public class DiabetesNormalPG implements Interpreter {
    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double lowerLimitFastingGlucose = 125;
        double upperLimitPostGlucose2h = 140;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return checkUpperBound(upperLimitPostGlucose2h, glucoseTwoH) && //
                checkLowerBound(lowerLimitFastingGlucose, fastingGlucose);
    }

    @Override
    public String getName() {
        return "Diabetes with Normal 2 hour Post-Glucose";
    }
}
