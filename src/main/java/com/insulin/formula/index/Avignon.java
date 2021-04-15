package com.insulin.formula.index;

import com.insulin.functional.CalculateIndex;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.shared.NumericConstants.BALANCE_AVIGNON;

public class Avignon implements CalculateIndex {
    private final CalculateIndex avignonSib = new AvignonSib();
    private final CalculateIndex avignonSih = new AvignonSih();

    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        double sibResult = avignonSib.calculate(mandatoryInformation);
        double sihResult = avignonSih.calculate(mandatoryInformation);

        return ((BALANCE_AVIGNON * sibResult) + sihResult) / 2;
    }
}
