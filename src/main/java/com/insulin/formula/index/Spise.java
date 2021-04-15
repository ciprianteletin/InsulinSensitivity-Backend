package com.insulin.formula.index;

import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.model.form.OptionalInsulinInformation;

import static com.insulin.formula.RangeChecker.checkLowerBound;
import static com.insulin.formula.ValueConverter.APPROXIMATE;
import static com.insulin.formula.ValueConverter.convertHDL;
import static com.insulin.utils.FormulaUtils.*;
import static com.insulin.validation.FormulaValidation.validateTyroAndHdl;
import static com.insulin.validation.FormulaValidation.validateWeightAndHeight;
import static java.lang.Math.pow;

public class Spise implements CalculateIndex, IndexInterpreter {
    private static final double lowerLimit = 6.61;

    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        validateWeightAndHeight(mandatoryInformation.getOptionalInformation(), "spice");
        validateTyroAndHdl(mandatoryInformation.getOptionalInformation(), "spise");
        OptionalInsulinInformation optionalInformation = mandatoryInformation.getOptionalInformation();
        double hdl = optionalInformation.getHdl();
        hdl = convertHDL(hdl, mandatoryInformation.getPlaceholders().getGlucosePlaceholder());
        double bmi = calculateBMI(optionalInformation);
        double tyro = optionalInformation.getThyroglobulin();

        return 600 * pow(hdl, 0.185) / pow(tyro, 0.2) / pow(bmi, 1.338);
    }

    @Override
    public String interpret(double result) {
        if (checkLowerBound(lowerLimit, result)) {
            return "Healthy";
        }
        return "Insulin Resistance!";
    }

    @Override
    public String getInterval() {
        return APPROXIMATE + lowerLimit;
    }
}
