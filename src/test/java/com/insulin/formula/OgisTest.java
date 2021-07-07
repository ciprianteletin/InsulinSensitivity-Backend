package com.insulin.formula;

import com.insulin.enumerations.Severity;
import com.insulin.exceptions.model.InputIndexException;
import com.insulin.formula.index.Ogis;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryIndexInformation;
import com.insulin.util.MandatoryInformationTestAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OgisTest {
    private final FormulaMarker formulaMarker = new Ogis();

    @Test
    public void test_nullWeight() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withHeight(180.)
                .build(23, "Male");

        Exception exception = assertThrows(InputIndexException.class, () -> formulaMarker.calculate(mandatoryInformation));

        assertThat(exception.getMessage()).isEqualTo("Expected value for field weight used for index Ogis");
    }

    @Test
    public void test_nullHeight() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withWeight(80.)
                .build(23, "Male");

        Exception exception = assertThrows(InputIndexException.class, () -> formulaMarker.calculate(mandatoryInformation));

        assertThat(exception.getMessage()).isEqualTo("Expected value for field height used for index Ogis");
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

        assertThat(result.getResult()).isEqualTo(398.3851);
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

        assertThat(result.getResult()).isEqualTo(398.3851);
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

        assertThat(result.getResult()).isEqualTo(398.3851);
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

        assertThat(result.getResult()).isEqualTo(398.3851);
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

        assertThat(result.getResult()).isEqualTo(460.5088);
        assertThat(result.getSeverity()).isEqualTo(Severity.DEFAULT);
        assertThat(result.getMessage()).isEqualTo("-");
        assertThat(result.getNormalRange()).isEqualTo("-");
    }

    @Test
    public void test_excelFormula() {
        assertThat(formulaMarker.generateExcelFormula(1))
                .isEqualTo("((( 0.0118 * (General!I3 - 90 * 0.05551) + 1) * (792 * (( 6.5 * (5.551 * 75 / (0.1640443958298 * POWER(General!Q3, 0.515)" + //
                        " * POWER(0.01 * General!R3, 0.422))) - 10000 * (General!J3 - General!I3) / 30 ) / General!I3 + 4514 / General!F3 ) / " + //
                        "(General!O3 - General!L3 + 1951))) + (SQRT(POWER((( 0.0118 * (General!I3 - 90 * 0.05551) + 1) * " + //
                        "(792 * (( 6.5 * (5.551 * 75 / (0.1640443958298 * POWER(General!Q3, 0.515) * POWER(0.01 * General!R3, 0.422))) - 10000 * " + //
                        "(General!J3 - General!I3) / 30 ) / General!I3 + 4514 / General!F3 ) / (General!O3 - General!L3 + 1951))), 2)" + //
                        " + 4 * 0.0118 * 173 * (General!I3 - 90 * 0.05551) * (792 * (( 6.5 * (5.551 * 75 / (0.1640443958298 * POWER(General!Q3, 0.515)" + //
                        " * POWER(0.01 * General!R3, 0.422))) - 10000 * (General!J3 - General!I3) / 30 ) / General!I3 + 4514 / General!F3 ) / " + //
                        "(General!O3 - General!L3 + 1951))))) / 2");
    }
}
