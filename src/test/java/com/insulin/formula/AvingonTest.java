package com.insulin.formula;

import com.insulin.enumerations.Severity;
import com.insulin.exceptions.model.InputIndexException;
import com.insulin.formula.index.Avingon;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryIndexInformation;
import com.insulin.util.MandatoryInformationTestAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AvingonTest {
    private final FormulaMarker formulaMarker = new Avingon();

    @Test
    public void test_nullWeight() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .build(23, "Male");

        Exception exception = assertThrows(InputIndexException.class, () -> formulaMarker.calculate(mandatoryInformation));

        assertThat(exception.getMessage()).isEqualTo("Expected value for field weight used for index Avingon");
    }

    @Test
    public void test_withDefaultMgUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withWeight(80.)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(276974.5454);
    }

    @Test
    public void test_withDefaultMmolPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMmolGlucose()
                .withDefaultPmolInsulin()
                .withWeight(80.)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(276974.5454);
    }

    @Test
    public void test_withDefaultMgPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultPmolInsulin()
                .withWeight(80.)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(276974.5454);
    }

    @Test
    public void test_withDefaultMmolUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMmolGlucose()
                .withDefaultUiInsulin()
                .withWeight(80.)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(276974.5454);
    }

    @Test
    public void test_withInputData() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .glucoseWithValues("mg/dL", 95., 110., 120., 100.)
                .insulinWithValues("pmol/L", 45., 55., 65., 60.)
                .withWeight(75.5)
                .build(54, "Female");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(540103.1579);
        assertThat(result.getSeverity()).isEqualTo(Severity.DEFAULT);
        assertThat(result.getMessage()).isEqualTo("-");
        assertThat(result.getNormalRange()).isEqualTo("-");
    }

    @Test
    public void test_excelFormula() {
        assertThat(formulaMarker.generateExcelFormula(1))
                .isEqualTo("((0.137 * 100000000 /(General!F3 * General!L2 * 150/General!Q3)) + 100000000 /(General!J3 * General!P2 * 150/General!Q3)) / 2");
    }
}
