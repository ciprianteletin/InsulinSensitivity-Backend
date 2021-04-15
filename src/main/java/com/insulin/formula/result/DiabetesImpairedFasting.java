package com.insulin.formula.result;

import com.insulin.functional.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

public class DiabetesImpairedFasting implements Interpreter {
    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double lowerLimitFastingGlucose = 99;
        double upperLimitFastingGlucose = 126;
        double lowerLimitPostGlucose2h = 199;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return (lowerLimitFastingGlucose < fastingGlucose && upperLimitFastingGlucose > fastingGlucose) //
                && glucoseTwoH > lowerLimitPostGlucose2h;
    }

    @Override
    public String getName() {
        return "Diabetes with Impaired Fasting Glucose";
    }
}
