package com.insulin.formula;

import com.insulin.enumerations.Severity;
import com.insulin.formula.index.HomaB;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryIndexInformation;
import com.insulin.util.MandatoryInformationTestAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HomaBTest {
    private final FormulaMarker formulaMarker = new HomaB();

    @Test
    public void test_withDefaultMgUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(48.6486);
    }

    @Test
    public void test_withDefaultMmolPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMmolGlucose()
                .withDefaultPmolInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(48.6486);
    }

    @Test
    public void test_withDefaultMgPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultPmolInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(48.6486);
    }

    @Test
    public void test_withDefaultMmolUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMmolGlucose()
                .withDefaultUiInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(48.6486);
    }

    @Test
    public void test_diabetes() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultPmolInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(48.6486);
        assertThat(result.getSeverity()).isEqualTo(Severity.DIABETES);
        assertThat(result.getMessage()).isEqualTo("Diabetes");
        assertThat(result.getNormalRange()).isEqualTo("113.1±30.56");
    }

    @Test
    public void test_healthy() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .glucoseWithValues("mg/dL", 95., 110., 120., 100.)
                .insulinWithValues("μIU/mL", 9., 25., 35., 30.)
                .build(54, "Female");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(101.25);
        assertThat(result.getSeverity()).isEqualTo(Severity.HEALTHY);
        assertThat(result.getMessage()).isEqualTo("Healthy");
        assertThat(result.getNormalRange()).isEqualTo("113.1±30.56");
    }

    @Test
    public void test_default() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .glucoseWithValues("mg/dL", 120., 140., 150., 140.)
                .insulinWithValues("μIU/mL", 25., 45., 55., 50.)
                .build(54, "Female");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(157.8947);
        assertThat(result.getSeverity()).isEqualTo(Severity.DEFAULT);
        assertThat(result.getMessage()).isEqualTo("-");
        assertThat(result.getNormalRange()).isEqualTo("113.1±30.56");
    }

    @Test
    public void test_excelFormula() {
        assertThat(formulaMarker.generateExcelFormula(1))
                .isEqualTo("(20 * General!L2) / (General!F3 - 3.5)");
    }
}
