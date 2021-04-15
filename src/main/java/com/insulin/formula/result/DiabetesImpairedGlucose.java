package com.insulin.formula.result;

import com.insulin.interfaces.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

import static com.insulin.formula.RangeChecker.checkInBetween;
import static com.insulin.formula.RangeChecker.checkLowerBound;

public class DiabetesImpairedGlucose implements Interpreter {
    @Override
    public boolean interpret(GlucoseMandatory glucoseMandatory) {
        double lowerLimitFastingGlucose = 125;
        double lowerLimitPostGlucose2h = 139;
        double upperLimitPostGlucose2h = 200;

        double fastingGlucose = glucoseMandatory.getFastingGlucose();
        double glucoseTwoH = glucoseMandatory.getGlucoseOneTwenty();

        return checkLowerBound(lowerLimitFastingGlucose, fastingGlucose) && //
                checkInBetween(lowerLimitPostGlucose2h, upperLimitPostGlucose2h, glucoseTwoH);
    }

    @Override
    public String getName() {
        return "Diabetes with Impaired Glucose Tolerance";
    }
}
