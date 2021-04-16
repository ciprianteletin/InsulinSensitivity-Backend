package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;
import org.springframework.data.util.Pair;

import static com.insulin.formula.ValueConverter.*;
import static com.insulin.utils.FormulaUtils.*;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.utils.IndexUtils.defaultPair;
import static com.insulin.validation.FormulaValidation.validateWeightAndHeight;


public class StumvollV1 implements CalculateIndex, IndexInterpreter {
    @Override
    public IndexResult calculate(MandatoryInsulinInformation mandatoryInformation) {
        validateWeightAndHeight(mandatoryInformation.getOptionalInformation(), "stumvoll");
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();

        double insulin120 = convertSingleInsulin(insulinMandatory.getInsulinOneTwenty(),
                mandatoryInformation.getPlaceholders().getInsulinPlaceholder(), "pmol/L");
        double bmi = calculateBMI(mandatoryInformation.getOptionalInformation());
        int age = mandatoryInformation.getAge();

        double result = 0.222 - 0.00333 * bmi - 0.0000779 * insulin120 - 0.000422 * age;
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
