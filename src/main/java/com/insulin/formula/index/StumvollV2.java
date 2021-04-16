package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;
import org.springframework.data.util.Pair;

import static com.insulin.formula.ValueConverter.*;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.utils.IndexUtils.defaultPair;

public class StumvollV2 implements CalculateIndex, IndexInterpreter {
    @Override
    public IndexResult calculate(MandatoryInsulinInformation mandatoryInformation) {
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();

        double fastingInsulin = convertSingleInsulin(insulinMandatory.getFastingInsulin(),
                mandatoryInformation.getInsulinMandatory().getInsulinPlaceholder(), "pmol/L");
        double insulin120 = convertSingleInsulin(insulinMandatory.getInsulinOneTwenty(),
                mandatoryInformation.getInsulinMandatory().getInsulinPlaceholder(), "pmol/L");
        double glucose120 = convertSingleGlucose(glucoseMandatory.getGlucoseOneTwenty(),
                mandatoryInformation.getGlucoseMandatory().getGlucosePlaceholder(), "mmol/L");

        double result = 0.156 - 0.0000459 * insulin120 - 0.000321 * fastingInsulin - 0.00541 * glucose120;
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
