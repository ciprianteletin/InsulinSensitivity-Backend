package com.insulin.formula.result;

import com.insulin.functional.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

public class DiabetesNormalFasting implements Interpreter {
    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double upperLimitFastingGlucose = 100;
        double lowerLimitPostGlucose2h = 199;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return fastingGlucose < upperLimitFastingGlucose && glucoseTwoH > lowerLimitPostGlucose2h;
    }

    @Override
    public String getName() {
        return "Diabetes with Normal Fasting Glucose";
    }
}