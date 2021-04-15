package com.insulin.formula.result;

import com.insulin.interfaces.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

public class InsulinResistanceThree implements Interpreter {
    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double upperLimitFastingGlucose = 99;
        double upperLimitPostGlucose2h = 139;
        double lowerLimitGlucose30 = 155;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();
        double glucoseThree = glucoseMandatory.getGlucoseThree();

        return fastingGlucose < upperLimitFastingGlucose && glucoseTwoH < upperLimitPostGlucose2h //
                && glucoseThree > lowerLimitGlucose30;
    }

    @Override
    public String getName() {
        return "Insulin Resistance - 30 min glucose";
    }
}
