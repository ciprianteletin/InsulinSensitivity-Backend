package com.insulin.formula.result;

import com.insulin.interfaces.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

public class ImpairedGlucoseTolerance implements Interpreter {
    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double upperLimitFastingGlucose = 99;
        double lowerLimitPostGlucose2h = 139;
        double upperLimitPostGlucose2h = 200;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return fastingGlucose < upperLimitFastingGlucose && //
                (lowerLimitPostGlucose2h < glucoseTwoH && glucoseTwoH < upperLimitPostGlucose2h);
    }

    @Override
    public String getName() {
        return "Impaired Glucose Tolerance: Prediabetes";
    }
}
