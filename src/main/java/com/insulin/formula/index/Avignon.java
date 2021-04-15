package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.shared.constants.NumericConstants.BALANCE_AVIGNON;

public class Avignon implements CalculateIndex, IndexInterpreter {
    private final CalculateIndex avignonSib = new AvignonSib();
    private final CalculateIndex avignonSih = new AvignonSih();

    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        double sibResult = avignonSib.calculate(mandatoryInformation);
        double sihResult = avignonSih.calculate(mandatoryInformation);

        return ((BALANCE_AVIGNON * sibResult) + sihResult) / 2;
    }

    @Override
    public String interpret(double result) {
        return null;
    }

    @Override
    public String getInterval() {
        return null;
    }
}
