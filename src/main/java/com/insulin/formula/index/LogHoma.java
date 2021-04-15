package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.RangeChecker.checkInBetween;
import static com.insulin.formula.ValueConverter.PLUS_MINUS;
import static java.lang.Math.log;

public class LogHoma implements CalculateIndex, IndexInterpreter {
    private final static double interpretValue = 1.0;
    private final static double fluctuation = 0.64;

    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        Homa homa = new Homa();
        return log(homa.calculate(mandatoryInformation));
    }

    @Override
    public String interpret(double result) {
        double lowerBound = interpretValue - fluctuation;
        double upperBound = interpretValue + fluctuation;
        if (checkInBetween(lowerBound, upperBound, result)) {
            return "Healthy";
        }
        return "Prediabetes";
    }

    @Override
    public String getInterval() {
        return interpretValue + PLUS_MINUS + fluctuation;
    }
}
