package com.insulin.excel;

import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.model.form.OptionalInsulinInformation;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

import static com.insulin.excel.utils.InformationPageUtils.*;
import static com.insulin.formula.ValueConverter.*;
import static java.util.Objects.isNull;

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
    private int rowNumber;
    private final Workbook workbook;
    private final Sheet sheet;
    private int infoId;

    public InformationSheet(Workbook workbook) {
        this.dataNames = getDataNamesList();
        sheet = generateInfoSheet();
        this.workbook = workbook;
        this.infoId = 1;
        this.rowNumber = 0;
    }

    public int getCurrentInfoId() {
        return this.infoId;
    }

    public void addMandatoryInformation(MandatoryInsulinInformation mandatoryInformation) {
        Row firstRow = sheet.createRow(this.rowNumber++);
        Row secondRow = sheet.createRow(this.rowNumber++);
        addUserInfo(firstRow, secondRow, mandatoryInformation);
        addGlucoseData(mandatoryInformation.getGlucoseMandatory(), firstRow, secondRow);
        addInsulinData(mandatoryInformation.getInsulinMandatory(), firstRow, secondRow);
        addOptionalData(mandatoryInformation.getOptionalInformation(),
                mandatoryInformation.getGlucoseMandatory().getGlucosePlaceholder(),
                firstRow, secondRow);
    }

    private void addUserInfo(Row firstRow, Row secondRow, MandatoryInsulinInformation mandatoryInformation) {
        int cellNr = 0;
        Cell cell = firstRow.createCell(cellNr++);
        cell.setCellValue(infoId);

        createEmptyColumn(firstRow, cellNr++);

        cell = firstRow.createCell(cellNr++);
        cell.setCellValue(getGender(mandatoryInformation.getGender()));

        cell = firstRow.createCell(cellNr);
        cell.setCellValue(mandatoryInformation.getAge());

        cellNr = 0;
        cell = secondRow.createCell(cellNr++);
        cell.setCellValue(infoId);

        createEmptyColumn(secondRow, cellNr++);
        // As the second row will contain the same age & gender, we use - instead, in order to avoid duplicates.
        cell = secondRow.createCell(cellNr++);
        cell.setCellValue('-');

        cell = secondRow.createCell(cellNr);
        cell.setCellValue('-');

        ++infoId;
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

    private Sheet generateInfoSheet() {
        Sheet sheet = createSheet();
        createHeader(sheet);
        return sheet;
    }

    /**
     * Method to create an empty cell on a specific position in the given row.
     * The simple statement could be used instead of calling the method, but
     * the method name has a clear intend.
     */
    private void createEmptyColumn(Row row, int cellNr) {
        row.createCell(cellNr);
    }

    private void createHeader(Sheet sheet) {
        CellStyle cellStyle = generateHeaderStyle();
        int cellNumber = 0;
        Row header = sheet.createRow(rowNumber++);
        Cell cell = createCell(header, cellNumber++, cellStyle);
        cell.setCellValue("Information id");
        createCell(header, cellNumber++, cellStyle); // we want a gap between id and the rest of data
        for (String name : dataNames) {
            cell = createCell(header, cellNumber++, cellStyle);
            cell.setCellValue(name);
        }
    }

    private void createCellWithValue(Row row, int cellNr, Double value) {
        Cell cell = row.createCell(cellNr);
        cell.setCellValue(value);
    }

    private void createCellWithValue(Row row, int cellNr, String value) {
        Cell cell = row.createCell(cellNr);
        cell.setCellValue(value);
    }

    /**
     * Method used for optional values that can be null. Replacing null with "-"
     */
    private void createCellWithNullCheck(Row row, int cellNr, Double value) {
        Cell cell = row.createCell(cellNr);
        if (isNull(value)) {
            cell.setCellValue("-");
            return;
        }
        cell.setCellValue(value);
    }

    private void createCellWithNullCheckCommons(Row firstRow, Row secondRow, int cellNr, Double value) {
        createCellWithNullCheck(firstRow, cellNr, value);
        createCellWithNullCheck(secondRow, cellNr, value);
    }

    private Cell createCell(Row row, int cellNumber, CellStyle cellStyle) {
        Cell cell = row.createCell(cellNumber);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    private Sheet createSheet() {
        Sheet sheet = workbook.createSheet("General");
        sheet.setDefaultColumnWidth(6000);
        return sheet;
    }

    private CellStyle generateHeaderStyle() {
        Font font = generateHeaderFont();
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);
        return style;
    }

    private Font generateHeaderFont() {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        font.setItalic(false);
        return font;
    }

    private String getGender(String gender) {
        if (gender.equals("M")) {
            return "Male";
        }
        return "Female";
    }
}
