package com.insulin.formula.result;

import com.insulin.interfaces.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

public class ImpairedFastingGlucose implements Interpreter {
    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double lowerLimitFastingGlucose = 99;
        double upperLimitFastingGlucose = 126;
        double upperLimitPostGlucose2h = 140;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return (lowerLimitFastingGlucose < fastingGlucose && upperLimitFastingGlucose > fastingGlucose) //
                && glucoseTwoH < upperLimitPostGlucose2h;
    }

    @Override
    public String getName() {
        return "Impaired Fasting Glucose: Prediabetes";
    }
}
