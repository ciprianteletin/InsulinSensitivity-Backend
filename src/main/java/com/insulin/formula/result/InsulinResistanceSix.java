package com.insulin.formula.result;

import com.insulin.interfaces.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

import static com.insulin.formula.RangeChecker.checkLowerBound;
import static com.insulin.formula.RangeChecker.checkUpperBound;

public class InsulinResistanceSix implements Interpreter {
    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double upperLimitFastingGlucose = 99;
        double upperLimitPostGlucose2h = 139;
        double lowerLimitGlucose60 = 155;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseSix = glucoseMandatory.getGlucoseSix();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return checkUpperBound(upperLimitFastingGlucose, fastingGlucose) && //
                checkUpperBound(upperLimitPostGlucose2h, glucoseTwoH) && //
                checkLowerBound(lowerLimitGlucose60, glucoseSix);
    }

    @Override
    public String getName() {
        return "Insulin Resistance - 60 min glucose";
    }
}
