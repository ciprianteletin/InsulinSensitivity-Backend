package com.insulin.formula.index;

import com.insulin.enumerations.Severity;
import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.IndexInterpreter;
import com.insulin.model.form.*;
import org.springframework.data.util.Pair;

import static com.insulin.shared.constants.NumericConstants.TEN_FOUR;
import static com.insulin.formula.ValueConverter.glucoseConverter;
import static com.insulin.formula.ValueConverter.insulinConverter;
import static com.insulin.utils.IndexUtils.buildIndexResult;
import static com.insulin.utils.IndexUtils.defaultPair;
import static com.insulin.validation.FormulaValidation.validateWeightAndHeight;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Ogis implements CalculateIndex, IndexInterpreter {
    @Override
    public IndexResult calculate(MandatoryInsulinInformation mandatoryInformation) {
        validateWeightAndHeight(mandatoryInformation.getOptionalInformation(), "ogis");
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        InsulinMandatory insulinMandatory = mandatoryInformation.getInsulinMandatory();

        glucoseMandatory = glucoseConverter(glucoseMandatory, "mmol/L");
        insulinMandatory = insulinConverter(insulinMandatory, "pmol/L");

        double glucoseNine = (glucoseMandatory.getGlucoseOneTwenty() + glucoseMandatory.getGlucoseSix()) / 2;
        double insulinNine = (insulinMandatory.getInsulinOneTwenty() + insulinMandatory.getInsulinSix()) / 2;

        double pp1 = 6.5, pp2 = 1951, pp3 = 4514;
        double pp4 = 792, pp5 = 0.0118, pp6 = 173;
        double dt = 30, glc = 90 * 0.05551;
        OptionalInsulinInformation optionalInformation = mandatoryInformation.getOptionalInformation();
        double bsa = 0.1640443958298 * pow(optionalInformation.getWeight(), 0.515)
                * pow((0.01 * optionalInformation.getHeight()), 0.422);
        double ndose = 5.551 * 75 / bsa;

        double temp1 = pp4 * ((pp1 * ndose - TEN_FOUR * (glucoseMandatory.getGlucoseOneTwenty() - glucoseNine) / dt) /
                glucoseNine + pp3 / glucoseMandatory.getFastingGlucose()) /
                (insulinNine - insulinMandatory.getFastingInsulin() + pp2);
        double temp2 = (pp5 * (glucoseNine - glc) + 1) * temp1;

        double result = (temp2 + sqrt(pow(temp2, 2) + 4 * pp5 * pp6 * (glucoseNine - glc) * temp1)) / 2;
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
