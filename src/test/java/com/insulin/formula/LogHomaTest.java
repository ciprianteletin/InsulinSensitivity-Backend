package com.insulin.formula;

import com.insulin.enumerations.Severity;
import com.insulin.formula.index.LogHoma;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryIndexInformation;
import com.insulin.util.MandatoryInformationTestAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LogHomaTest {
    private final FormulaMarker formulaMarker = new LogHoma();

    @Test
    public void test_withDefaultMgUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.2107);
    }

    @Test
    public void test_withDefaultMmolPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMmolGlucose()
                .withDefaultPmolInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.2107);
    }

    @Test
    public void test_withDefaultMgPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultPmolInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.2107);
    }

    @Test
    public void test_withDefaultMmolUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMmolGlucose()
                .withDefaultUiInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.2107);
    }

    @Test
    public void test_prediabetes() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultPmolInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.2107);
        assertThat(result.getSeverity()).isEqualTo(Severity.PREDIABETES);
        assertThat(result.getMessage()).isEqualTo("Prediabetes");
        assertThat(result.getNormalRange()).isEqualTo("1.0±0.64");
    }

    @Test
    public void test_healthy() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .glucoseWithValues("mg/dL", 95., 110., 120., 100.)
                .insulinWithValues("μIU/mL", 9., 25., 35., 30.)
                .build(54, "Female");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.7472);
        assertThat(result.getSeverity()).isEqualTo(Severity.HEALTHY);
        assertThat(result.getMessage()).isEqualTo("Healthy");
        assertThat(result.getNormalRange()).isEqualTo("1.0±0.64");
    }

    @Test
    public void test_excelFormula() {
        assertThat(formulaMarker.generateExcelFormula(1))
                .isEqualTo("LN((General!F3 * General!L2)/ 22.5)");
    }
}
