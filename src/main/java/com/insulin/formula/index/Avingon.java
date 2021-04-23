package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryInsulinInformation;
import org.springframework.data.util.Pair;

import static com.insulin.shared.constants.NumericConstants.BALANCE_AVIGNON;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.utils.IndexUtils.defaultPair;

public class Avingon implements CalculateIndex, IndexInterpreter {
    private final CalculateIndex avingonSib = new AvingonSib();
    private final CalculateIndex avingonSih = new AvingonSih();

    @Override
    public IndexResult calculate(MandatoryInsulinInformation mandatoryInformation) {
        double sibResult = avingonSib.calculate(mandatoryInformation).getResult();
        double sihResult = avingonSih.calculate(mandatoryInformation).getResult();

        double result = ((BALANCE_AVIGNON * sibResult) + sihResult) / 2;
        return buildIndexResult(result, interpret(result));
    }

    @Override
    public Pair<String, Severity> interpret(double result) {
        return defaultPair();
    }

    @Override
    public String getInterval() {
        return "-";
    }
}
