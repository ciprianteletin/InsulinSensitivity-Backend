package com.insulin.formula.result;

import com.insulin.interfaces.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

public class Diabetes implements Interpreter {

    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double lowerLimitFastingGlucose = 125;
        double lowerLimitPostGlucose2h = 199;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return fastingGlucose > lowerLimitFastingGlucose && glucoseTwoH > lowerLimitPostGlucose2h;
    }

    @Override
    public String getName() {
        return "Diabetes";
    }
}
