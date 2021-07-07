package com.insulin.formula;

import com.insulin.enumerations.Severity;
import com.insulin.formula.index.Homa;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryIndexInformation;
import com.insulin.util.MandatoryInformationTestAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HomaTest {
    private final FormulaMarker formulaMarker = new Homa();

    @Test
    public void test_withDefaultMgUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(1.2346);
    }

    @Test
    public void test_withDefaultMmolPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMmolGlucose()
                .withDefaultPmolInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(1.2346);
    }

    @Test
    public void test_withDefaultMgPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultPmolInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(1.2346);
    }

    @Test
    public void test_withDefaultMmolUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMmolGlucose()
                .withDefaultUiInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(1.2346);
    }

    @Test
    public void test_healthy() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultPmolInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(1.2346);
        assertThat(result.getSeverity()).isEqualTo(Severity.HEALTHY);
        assertThat(result.getMessage()).isEqualTo("Healthy");
        assertThat(result.getNormalRange()).isEqualTo("0.5-1.4");
    }

    @Test
    public void test_earlyInsulinResistance() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .glucoseWithValues("mg/dL", 95., 110., 120., 100.)
                .insulinWithValues("μIU/mL", 9., 25., 35., 30.)
                .build(54, "Female");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(2.1111);
        assertThat(result.getSeverity()).isEqualTo(Severity.INSULIN_RESISTANCE);
        assertThat(result.getMessage()).isEqualTo("Early Insulin Resistance");
        assertThat(result.getNormalRange()).isEqualTo("0.5-1.4");
    }

    @Test
    public void test_insulinResistance() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .glucoseWithValues("mg/dL", 120., 140., 150., 140.)
                .insulinWithValues("μIU/mL", 25., 45., 55., 50.)
                .build(54, "Female");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(7.4074);
        assertThat(result.getSeverity()).isEqualTo(Severity.INSULIN_RESISTANCE);
        assertThat(result.getMessage()).isEqualTo("Insulin Resistance");
        assertThat(result.getNormalRange()).isEqualTo("0.5-1.4");
    }

    @Test
    public void test_excelFormula() {
        assertThat(formulaMarker.generateExcelFormula(1))
                .isEqualTo("(General!F3 * General!L2)/ 22.5");
    }
}
