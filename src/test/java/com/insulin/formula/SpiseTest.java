package com.insulin.formula;

import com.insulin.exceptions.model.InputIndexException;
import com.insulin.formula.index.Spise;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryIndexInformation;
import com.insulin.util.MandatoryInformationTestAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpiseTest {
    private final FormulaMarker formulaMarker = new Spise();

    @Test
    public void test_nullWeight() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withHeight(180.)
                .build(23, "Male");

        Exception exception = assertThrows(InputIndexException.class, () -> formulaMarker.calculate(mandatoryInformation));

        assertThat(exception.getMessage()).isEqualTo("Expected value for field weight used for index Spise");
    }

    @Test
    public void test_nullHeight() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withWeight(80.)
                .build(23, "Male");

        Exception exception = assertThrows(InputIndexException.class, () -> formulaMarker.calculate(mandatoryInformation));

        assertThat(exception.getMessage()).isEqualTo("Expected value for field height used for index Spise");
    }

    @Test
    public void test_nullHdl() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withHeight(180.)
                .withWeight(80.)
                .withThyroglobulin(100.)
                .build(23, "Male");

        Exception exception = assertThrows(InputIndexException.class, () -> formulaMarker.calculate(mandatoryInformation));

        assertThat(exception.getMessage()).isEqualTo("Null value detected for tyroglobulin or hdl for index Spise");
    }

    @Test
    public void test_nullThyroglobulin() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withHeight(180.)
                .withWeight(80.)
                .withHDL(100.)
                .build(23, "Male");

        Exception exception = assertThrows(InputIndexException.class, () -> formulaMarker.calculate(mandatoryInformation));

        assertThat(exception.getMessage()).isEqualTo("Null value detected for tyroglobulin or hdl for index Spise");
    }

    @Test
    public void test_withDefaultMgUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withHeight(180.)
                .withWeight(80.)
                .withHDL(55.)
                .withThyroglobulin(100.)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(3.4932);
    }

    @Test
    public void test_withDefaultMmolPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withHeight(180.)
                .withWeight(80.)
                .withHDL(55.)
                .withThyroglobulin(100.)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(3.4932);
    }

    @Test
    public void test_withDefaultMgPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withHeight(180.)
                .withWeight(80.)
                .withHDL(55.)
                .withThyroglobulin(100.)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(3.4932);
    }

    @Test
    public void test_withDefaultMmolUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withHeight(180.)
                .withWeight(80.)
                .withHDL(55.)
                .withThyroglobulin(100.)
                .build(23, "Male");


        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(3.4932);
    }


    @Test
    public void test_excelFormula() {
        assertThat(formulaMarker.generateExcelFormula(1))
                .isEqualTo("600 * POWER(General!S3, 0.185) / POWER(General!V3, 0.2) / POWER(General!Q3 / POWER(General!R3 / 100, 2), 1.338)");
    }
}
