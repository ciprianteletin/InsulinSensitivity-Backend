package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;

import static com.insulin.formula.ValueConverter.*;

public class StumvollV2 implements CalculateIndex, IndexInterpreter {
    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();

        double fastingInsulin = convertSingleInsulin(insulinMandatory.getFastingInsulin(),
                mandatoryInformation.getPlaceholders().getInsulinPlaceholder(), "pmol/L");
        double insulin120 = convertSingleInsulin(insulinMandatory.getInsulinOneTwenty(),
                mandatoryInformation.getPlaceholders().getInsulinPlaceholder(), "pmol/L");
        double glucose120 = convertSingleGlucose(glucoseMandatory.getGlucoseOneTwenty(),
                mandatoryInformation.getPlaceholders().getGlucosePlaceholder(), "mmol/L");

        return 0.156 - 0.0000459 * insulin120 - 0.000321 * fastingInsulin - 0.00541 * glucose120;
    }

    @Override
    public String interpret(double result) {
        return "-";
    }

    @Override
    public String getInterval() {
        return "-";
    }
}
