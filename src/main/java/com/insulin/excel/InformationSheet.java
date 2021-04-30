package com.insulin.excel;

import com.insulin.excel.utils.RowTracker;
import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.model.form.OptionalInsulinInformation;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

import static com.insulin.excel.utils.ExcelCommons.*;
import static com.insulin.excel.utils.InformationPageUtils.*;
import static com.insulin.formula.ValueConverter.*;

/**
 * InformationSheet class represents the first page of the Excel document which contains all
 * data inserted by the user. The data inserted will be used also as data for index formulas.
 * <p>
 * The data inserted will come in two versions, for each data type used. This cast is necessary
 * because each formula can use different unit of measurement for the same indicator.
 * The convention used will be:
 * Odd rows for mg/dL and uUi/mL
 * Even rows for mmol/L and pmol/L
 * Both rows will share the same id, so that indices that depends on that id will have a idea of
 * the place where the search will be made.
 */
public class InformationSheet {
    private final List<String> dataNames;
    private final Workbook workbook;
    private final Sheet sheet;
    private int infoId;
    private final RowTracker tracker;

    public InformationSheet(Workbook workbook) {
        this.dataNames = getDataNamesList();
        this.tracker = new RowTracker();
        tracker.addNewRecord(infoSheetName, 0);
        this.workbook = workbook;
        sheet = createSheet();
        this.infoId = 1;
    }

    /**
     * The information id is used to calculate the formula with the expected data.
     * The information of any formula depends on this information. The data in mg/dL and uUi/mL will be find on row infoId * 3 - 1.
     * For example, for infoId = 1, the values will be on row 2 and 3
     */
    public int getCurrentInfoId() {
        return this.infoId;
    }

    public void increaseInfoId() {
        this.infoId++;
    }

    public void addMandatoryInformation(MandatoryInsulinInformation mandatoryInformation) {
        Row firstRow = sheet.createRow(this.tracker.getAndUpdate(infoSheetName));
        Row secondRow = sheet.createRow(this.tracker.getAndUpdate(infoSheetName));
        addUserInfo(firstRow, secondRow, mandatoryInformation);
        addGlucoseData(mandatoryInformation.getGlucoseMandatory(), firstRow, secondRow);
        addInsulinData(mandatoryInformation.getInsulinMandatory(), firstRow, secondRow);
        addOptionalData(mandatoryInformation.getOptionalInformation(),
                mandatoryInformation.getGlucoseMandatory().getGlucosePlaceholder(),
                firstRow, secondRow);
        createEmptyRow(this.tracker.getAndUpdate(infoSheetName), this.sheet);
    }

    private void addUserInfo(Row firstRow, Row secondRow, MandatoryInsulinInformation mandatoryInformation) {
        int cellNr = 0;

        createCellWithValue(firstRow, cellNr++, infoId);
        createEmptyColumn(firstRow, cellNr++);
        createCellWithValue(firstRow, cellNr++, mandatoryInformation.getGender());
        createCellWithValue(firstRow, cellNr, mandatoryInformation.getAge());

        cellNr = 0;

        createCellWithValue(secondRow, cellNr++, infoId);
        createEmptyColumn(secondRow, cellNr++);
        // As the second row will contain the same age & gender, we use - instead, in order to avoid duplicates.
        createCellWithValue(secondRow, cellNr++, "-");
        createCellWithValue(secondRow, cellNr, "-");
    }

    private void addGlucoseData(GlucoseMandatory glucoseMandatory, Row firstRow, Row secondRow) {
        String placeholder = glucoseMandatory.getGlucosePlaceholder();
        if (placeholder.equals("mg/dL")) {
            addGlucose(glucoseMandatory, firstRow);
            GlucoseMandatory changedGlucose = glucoseConverter(glucoseMandatory, "mmol/L");
            addGlucose(changedGlucose, secondRow);
        } else {
            addGlucose(glucoseMandatory, secondRow);
            GlucoseMandatory changedGlucose = glucoseConverter(glucoseMandatory, "mg/dL");
            addGlucose(changedGlucose, firstRow);
        }
    }

    private void addInsulinData(InsulinMandatory insulinMandatory, Row firstRow, Row secondRow) {
        String placeholder = insulinMandatory.getInsulinPlaceholder();
        if (placeholder.equals("μIU/mL")) {
            addInsulin(insulinMandatory, firstRow);
            InsulinMandatory changedInsulin = insulinConverter(insulinMandatory, "pmol/L");
            addInsulin(changedInsulin, secondRow);
        } else {
            addInsulin(insulinMandatory, secondRow);
            InsulinMandatory changedInsulin = insulinConverter(insulinMandatory, "μIU/mL");
            addInsulin(changedInsulin, firstRow);
        }
    }

    private void addOptionalData(OptionalInsulinInformation optionalInformation, String placeholder, Row first, Row second) {
        int cellNr = OPTIONAL_START;
        createCellWithNullCheckCommons(first, second, cellNr++, optionalInformation.getWeight());
        createCellWithNullCheckCommons(first, second, cellNr, optionalInformation.getHeight());

        if (placeholder.equals("mg/dL")) {
            addOptional(optionalInformation, first);
            OptionalInsulinInformation convertedOpt = optionalConverter(optionalInformation, placeholder, "mmol/L");
            addOptional(convertedOpt, second);
        } else {
            addOptional(optionalInformation, second);
            OptionalInsulinInformation convertedOpt = optionalConverter(optionalInformation, placeholder, "mg/dL");
            addOptional(convertedOpt, first);
        }
    }

    private void addGlucose(GlucoseMandatory glucoseMandatory, Row row) {
        int cellNr = GLUCOSE_START;

        createCellWithValue(row, cellNr++, glucoseMandatory.getGlucosePlaceholder());
        createCellWithValue(row, cellNr++, glucoseMandatory.getFastingGlucose());
        createCellWithValue(row, cellNr++, glucoseMandatory.getGlucoseThree());
        createCellWithValue(row, cellNr++, glucoseMandatory.getGlucoseSix());
        double glucoseNine = (glucoseMandatory.getGlucoseSix() + glucoseMandatory.getGlucoseOneTwenty()) / 2;
        glucoseNine = Math.round(glucoseNine * 100.0) / 100.0;
        createCellWithValue(row, cellNr++, glucoseNine);
        createCellWithValue(row, cellNr, glucoseMandatory.getGlucoseOneTwenty());
    }

    private void addInsulin(InsulinMandatory insulinMandatory, Row row) {
        int cellNr = INSULIN_START;

        createCellWithValue(row, cellNr++, insulinMandatory.getInsulinPlaceholder());
        createCellWithValue(row, cellNr++, insulinMandatory.getFastingInsulin());
        createCellWithValue(row, cellNr++, insulinMandatory.getInsulinThree());
        createCellWithValue(row, cellNr++, insulinMandatory.getInsulinSix());
        double insulinNine = (insulinMandatory.getInsulinSix() + insulinMandatory.getInsulinOneTwenty()) / 2;
        insulinNine = Math.round(insulinNine * 100.0) / 100.0;
        createCellWithValue(row, cellNr++, insulinNine);
        createCellWithValue(row, cellNr, insulinMandatory.getInsulinOneTwenty());
    }

    private void addOptional(OptionalInsulinInformation optionalinformation, Row row) {
        int cellNr = OPTIONAL_START + 2; // as we added two values before

        createCellWithNullCheck(row, cellNr++, optionalinformation.getHdl());
        createCellWithNullCheck(row, cellNr++, optionalinformation.getNefa());
        createCellWithNullCheck(row, cellNr++, optionalinformation.getTriglyceride());
        createCellWithNullCheck(row, cellNr, optionalinformation.getThyroglobulin());
    }

    private Sheet createSheet() {
        Sheet sheet = workbook.createSheet("General");
        createHeader(sheet);
        return sheet;
    }

    private void createHeader(Sheet sheet) {
        CellStyle cellStyle = generateHeaderStyle(workbook);
        int cellNumber = 0;
        Row header = sheet.createRow(this.tracker.getAndUpdate(infoSheetName));
        createCellWithStyleAndValue(header, cellNumber++, cellStyle, "Information id");
        createCellWithStyle(header, cellNumber++, cellStyle); // we want a gap between id and the rest of data
        for (String name : dataNames) {
            createCellWithStyleAndValue(header, cellNumber++, cellStyle, name);
        }
        int columns = dataNames.size() + 2;
        for (int i = 0; i < columns; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
