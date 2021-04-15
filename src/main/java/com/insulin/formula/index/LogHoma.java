package com.insulin.formula.index;

import com.insulin.functional.CalculateIndex;
import com.insulin.model.form.MandatoryInsulinInformation;

import static java.lang.Math.log;

public class LogHoma implements CalculateIndex {
    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        Homa homa = new Homa();
        return log(homa.calculate(mandatoryInformation));
    }
}
