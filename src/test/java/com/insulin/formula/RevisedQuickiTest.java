package com.insulin.formula;

import com.insulin.enumerations.Severity;
import com.insulin.exceptions.model.InputIndexException;
import com.insulin.formula.index.RevisedQuicki;
import com.insulin.interfaces.FormulaMarker;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryIndexInformation;
import com.insulin.util.MandatoryInformationTestAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RevisedQuickiTest {
    private final FormulaMarker formulaMarker = new RevisedQuicki();


    @Test
    public void test_nullNefa() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .build(23, "Male");

        Exception exception = assertThrows(InputIndexException.class, () -> formulaMarker.calculate(mandatoryInformation));

        assertThat(exception.getMessage()).isEqualTo("Expected value for field nefa used for index Revised Quicki");
    }

    @Test
    public void test_withDefaultMgUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultUiInsulin()
                .withNefa(55.)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.1592);
    }

    @Test
    public void test_withDefaultMmolPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMmolGlucose()
                .withDefaultPmolInsulin()
                .withNefa(55. * 0.35)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.1592);
    }

    @Test
    public void test_withDefaultMgPmol() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultPmolInsulin()
                .withNefa(55.)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.1592);
    }

    @Test
    public void test_withDefaultMmolUi() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMmolGlucose()
                .withDefaultUiInsulin()
                .withNefa(55. * 0.35)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.1592);
    }

    @Test
    public void test_default() {
        MandatoryIndexInformation mandatoryInformation = MandatoryInformationTestAPI.buildMandatoryInformation()
                .withDefaultMgGlucose()
                .withDefaultPmolInsulin()
                .withNefa(55.)
                .build(23, "Male");

        IndexResult result = formulaMarker.calculate(mandatoryInformation);

        assertThat(result.getResult()).isEqualTo(0.1592);
        assertThat(result.getSeverity()).isEqualTo(Severity.DEFAULT);
        assertThat(result.getMessage()).isEqualTo("-");
        assertThat(result.getNormalRange()).isEqualTo("0.448±0.013");
    }


    @Test
    public void test_excelFormula() {
        assertThat(formulaMarker.generateExcelFormula(1))
                .isEqualTo("1.0 / (LN(General!F3) + LN(General!L2) + LN(General!T3))");
    }
}
