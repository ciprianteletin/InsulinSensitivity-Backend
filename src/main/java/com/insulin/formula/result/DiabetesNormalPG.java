package com.insulin.formula.result;

import com.insulin.functional.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

public class DiabetesNormalPG implements Interpreter {
    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double lowerLimitFastingGlucose = 125;
        double upperLimitPostGlucose2h = 140;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return lowerLimitFastingGlucose < fastingGlucose && glucoseTwoH < upperLimitPostGlucose2h;
    }

    @Override
    public String getName() {
        return "Diabetes with Normal 2 hour Post-Glucose";
    }
}
