package com.insulin.formula.result;

import com.insulin.interfaces.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

import static com.insulin.formula.RangeChecker.checkInBetween;
import static com.insulin.formula.RangeChecker.checkLowerBound;

public class DiabetesImpairedFasting implements Interpreter {
    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double lowerLimitFastingGlucose = 99;
        double upperLimitFastingGlucose = 126;
        double lowerLimitPostGlucose2h = 199;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return checkInBetween(lowerLimitFastingGlucose, upperLimitFastingGlucose, fastingGlucose) //
                && checkLowerBound(lowerLimitPostGlucose2h, glucoseTwoH);
    }

    @Override
    public String getName() {
        return "Diabetes with Impaired Fasting Glucose";
    }
}
