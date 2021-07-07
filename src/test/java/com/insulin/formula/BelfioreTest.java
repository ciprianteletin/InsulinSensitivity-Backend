package com.insulin.formula;

import com.insulin.enumerations.Severity;
import com.insulin.formula.index.Belfiore;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryIndexInformation;
import com.insulin.util.MandatoryInformationTestAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class BelfioreTest {
    private final FormulaMarker formulaMarker = new Belfiore();

    @Test
    public void test_withDefaultMgUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(1.2589);
    }

    @Test
    public void test_withDefaultMmolPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMmolGlucose()
                .withDefaultPmolInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(1.2589);
    }

    @Test
    public void test_withDefaultMgPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultPmolInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(1.2589);
    }

    @Test
    public void test_withDefaultMmolUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMmolGlucose()
                .withDefaultUiInsulin()
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(1.2589);
    }

    @Test
    public void test_insulinResistance() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .glucoseWithValues("mg/dL", 95., 110., 120., 100.)
                .insulinWithValues("pmol/L", 45., 55., 65., 60.)
                .build(54, "Female");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(1.689);
        assertThat(result.getSeverity()).isEqualTo(Severity.INSULIN_RESISTANCE);
        assertThat(result.getMessage()).isEqualTo("Insulin resistance");
        assertThat(result.getNormalRange()).isEqualTo("≅1");
    }

    @Test
    public void test_healthy() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .glucoseWithValues("mg/dL", 98., 110., 120., 100.)
                .insulinWithValues("μIU/mL", 8., 55., 45., 40.)
                .build(54, "Female");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(1.1974);
        assertThat(result.getSeverity()).isEqualTo(Severity.HEALTHY);
        assertThat(result.getMessage()).isEqualTo("Healthy");
        assertThat(result.getNormalRange()).isEqualTo("≅1");
    }

    @Test
    public void test_diabetes() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .glucoseWithValues("mg/dL", 150., 220., 215., 200.)
                .insulinWithValues("μIU/mL", 25., 55., 75., 65.)
                .build(54, "Female");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.6468);
        assertThat(result.getSeverity()).isEqualTo(Severity.DIABETES);
        assertThat(result.getMessage()).isEqualTo("Type 2 diabetes");
        assertThat(result.getNormalRange()).isEqualTo("≅1");
    }

    @Test
    public void test_excelFormula() {
        assertThat(formulaMarker.generateExcelFormula(1))
                .isEqualTo("2 / (((0.5 * General!F3 + General!H3 + General!J3) / 19.08) * ((0.5 * General!L2 + General!N2 + General!P2) / 104.0) + 1 )");
    }
}
