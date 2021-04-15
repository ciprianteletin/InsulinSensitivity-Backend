package com.insulin.formula.result;

import com.insulin.interfaces.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

public class ImpairedFastingPost implements Interpreter {
    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double lowerLimitFastingGlucose = 99;
        double upperLimitFastingGlucose = 126;
        double lowerLimitPostGlucose2h = 139;
        double upperLimitPostGlucose2h = 200;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return (lowerLimitFastingGlucose < fastingGlucose && upperLimitFastingGlucose > fastingGlucose) //
                && (lowerLimitPostGlucose2h < glucoseTwoH && glucoseTwoH < upperLimitPostGlucose2h);
    }

    @Override
    public String getName() {
        return "Impaired Fasting Glucose and Impaired Glucose Tolerance: Prediabetes";
    }
}
