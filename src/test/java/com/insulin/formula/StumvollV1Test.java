package com.insulin.formula;

import com.insulin.enumerations.Severity;
import com.insulin.exceptions.model.InputIndexException;
import com.insulin.formula.index.StumvollV1;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryIndexInformation;
import com.insulin.util.MandatoryInformationTestAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StumvollV1Test {
    private final FormulaMarker formulaMarker = new StumvollV1();

    @Test
    public void test_nullWeight() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withHeight(180.)
                .build(23, "Male");

        Exception exception = assertThrows(InputIndexException.class, () -> formulaMarker.calculate(mandatoryInformation));

        assertThat(exception.getMessage()).isEqualTo("Expected value for field weight used for index Stumvoll");
    }

    @Test
    public void test_nullHeight() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withWeight(80.)
                .build(23, "Male");

        Exception exception = assertThrows(InputIndexException.class, () -> formulaMarker.calculate(mandatoryInformation));

        assertThat(exception.getMessage()).isEqualTo("Expected value for field height used for index Stumvoll");
    }

    @Test
    public void test_withDefaultMgUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withWeight(80.)
                .withHeight(180.)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.116);
    }

    @Test
    public void test_withDefaultMmolPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMmolGlucose()
                .withDefaultPmolInsulin()
                .withWeight(80.)
                .withHeight(180.)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.116);
    }

    @Test
    public void test_withDefaultMgPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultPmolInsulin()
                .withWeight(80.)
                .withHeight(180.)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.116);
    }

    @Test
    public void test_withDefaultMmolUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMmolGlucose()
                .withDefaultUiInsulin()
                .withWeight(80.)
                .withHeight(180.)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.116);
    }

    @Test
    public void test_withInputData() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .glucoseWithValues("mg/dL", 95., 110., 120., 100.)
                .insulinWithValues("pmol/L", 45., 55., 65., 60.)
                .withWeight(75.5)
                .withHeight(160.2)
                .build(54, "Female");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.0966);
        assertThat(result.getSeverity()).isEqualTo(Severity.DEFAULT);
        assertThat(result.getMessage()).isEqualTo("-");
        assertThat(result.getNormalRange()).isEqualTo("-");
    }

    @Test
    public void test_excelFormula() {
        assertThat(formulaMarker.generateExcelFormula(1))
                .isEqualTo("0.222 - 0.00333 * General!Q3 / POWER(General!R3 / 100, 2) - 0.0000779 *General!P3 - 0.000422 * General!D2");
    }
}
