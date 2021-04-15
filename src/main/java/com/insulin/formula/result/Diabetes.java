package com.insulin.formula.result;

import com.insulin.interfaces.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

import static com.insulin.formula.RangeChecker.checkLowerBound;

public class Diabetes implements Interpreter {

    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double lowerLimitFastingGlucose = 125;
        double lowerLimitPostGlucose2h = 199;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return checkLowerBound(lowerLimitFastingGlucose, fastingGlucose) &&
                checkLowerBound(lowerLimitPostGlucose2h, glucoseTwoH);
    }

    @Override
    public String getName() {
        return "Diabetes";
    }
}
