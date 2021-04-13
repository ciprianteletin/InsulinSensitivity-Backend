package com.insulin.formula;

import com.insulin.functional.CalculateIndex;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.model.form.OptionalInsulinInformation;

import static com.insulin.formula.ValueConverter.convertHDL;
import static com.insulin.utils.FormulaUtils.*;
import static java.lang.Math.pow;

public class Spise implements CalculateIndex {
    @Override
    public double calculate(MandatoryInsulinInformation mandatoryInformation) {
        OptionalInsulinInformation optionalInformation = mandatoryInformation.getOptionalInformation();
        double hdl = optionalInformation.getHdl();
        hdl = convertHDL(hdl, mandatoryInformation.getPlaceholders().getGlucosePlaceholder());
        double bmi = calculateBMI(optionalInformation);
        double tyro = optionalInformation.getThyroglobulin();

        return 600 * pow(hdl, 0.185) / pow(tyro, 0.2) / pow(bmi, 1.338);
    }
}
