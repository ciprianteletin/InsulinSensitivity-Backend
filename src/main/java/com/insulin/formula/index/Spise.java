package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.model.form.OptionalInsulinInformation;
import org.springframework.data.util.Pair;

import static com.insulin.formula.RangeChecker.checkLowerBound;
import static com.insulin.formula.ValueConverter.APPROXIMATE;
import static com.insulin.formula.ValueConverter.convertHDL;
import static com.insulin.utils.FormulaUtils.*;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.utils.IndexUtils.healthyPair;
import static com.insulin.validation.FormulaValidation.validateTyroAndHdl;
import static com.insulin.validation.FormulaValidation.validateWeightAndHeight;
import static java.lang.Math.pow;

public class Spise implements CalculateIndex, IndexInterpreter {
    private static final double lowerLimit = 6.61;

    @Override
    public IndexResult calculate(MandatoryInsulinInformation mandatoryInformation) {
        validateWeightAndHeight(mandatoryInformation.getOptionalInformation(), "spice");
        validateTyroAndHdl(mandatoryInformation.getOptionalInformation(), "spise");
        OptionalInsulinInformation optionalInformation = mandatoryInformation.getOptionalInformation();
        double hdl = optionalInformation.getHdl();
        hdl = convertHDL(hdl, mandatoryInformation.getGlucoseMandatory().getGlucosePlaceholder());
        double bmi = calculateBMI(optionalInformation);
        double tyro = optionalInformation.getThyroglobulin();

        double result = 600 * pow(hdl, 0.185) / pow(tyro, 0.2) / pow(bmi, 1.338);
        return buildIndexResult(result, interpret(result), getInterval());
    }

    @Override
    public Pair<String, Severity> interpret(double result) {
        if (checkLowerBound(lowerLimit, result)) {
            return healthyPair();
        }
        return Pair.of("Insulin Resistance", Severity.INSULIN_RESISTANCE);
    }

    @Override
    public String getInterval() {
        return APPROXIMATE + lowerLimit;
    }
}
