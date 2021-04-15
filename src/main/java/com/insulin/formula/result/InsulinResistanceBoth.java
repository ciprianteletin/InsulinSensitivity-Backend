package com.insulin.formula.result;

import com.insulin.interfaces.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

import static com.insulin.formula.RangeChecker.checkLowerBound;
import static com.insulin.formula.RangeChecker.checkUpperBound;

public class InsulinResistanceBoth implements Interpreter {
    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double upperLimitFastingGlucose = 99;
        double upperLimitPostGlucose2h = 139;
        double lowerLimitGlucose3060 = 155;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseThree = glucoseMandatory.getGlucoseThree();
        double glucoseSix = glucoseMandatory.getGlucoseSix();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return checkUpperBound(upperLimitFastingGlucose, fastingGlucose) && //
                checkUpperBound(upperLimitPostGlucose2h, glucoseTwoH) && //
                checkLowerBound(lowerLimitGlucose3060, glucoseSix) && //
                checkLowerBound(lowerLimitGlucose3060, glucoseThree);
    }

    @Override
    public String getName() {
        return "Insulin Resistance - 30 and 60 min glucose";
    }
}
