package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryIndexInformation;
import org.springframework.data.util.Pair;

import static com.insulin.shared.constants.NumericConstants.BALANCE_AVIGNON;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.utils.IndexUtils.defaultPair;

public class Avingon implements FormulaMarker, IndexInterpreter {
    private final FormulaMarker avingonSib = new AvingonSib();
    private final FormulaMarker avingonSih = new AvingonSih();

    @Override
    public IndexResult calculate(MandatoryIndexInformation mandatoryInformation) {
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

    @Override
    public String generateExcelFormula(int infoId) {
        StringBuilder formulaBuilder = new StringBuilder();

        String avingonSib = this.avingonSib.generateExcelFormula(infoId);
        String avingonSih = this.avingonSih.generateExcelFormula(infoId);

        formulaBuilder.append("((").append(BALANCE_AVIGNON).append(" * ")
                .append(avingonSib).append(") + ").append(avingonSih)
                .append(") / 2");
        return formulaBuilder.toString();
    }
}
