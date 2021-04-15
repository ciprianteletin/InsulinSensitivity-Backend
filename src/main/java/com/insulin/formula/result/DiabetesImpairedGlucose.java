package com.insulin.formula.result;

import com.insulin.functional.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

public class DiabetesImpairedGlucose implements Interpreter {
    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double lowerLimitFastingGlucose = 125;
        double lowerLimitPostGlucose2h = 139;
        double upperLimitPostGlucose2h = 200;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return fastingGlucose > lowerLimitFastingGlucose && //
                (lowerLimitPostGlucose2h < glucoseTwoH && upperLimitPostGlucose2h > glucoseTwoH);
    }

    @Override
    public String getName() {
        return "Diabetes with Impaired Glucose Tolerance";
    }
}
