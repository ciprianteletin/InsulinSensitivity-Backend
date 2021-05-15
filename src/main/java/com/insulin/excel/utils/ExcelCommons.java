package com.insulin.excel.utils;

import org.apache.poi.ss.usermodel.*;

import static java.util.Objects.isNull;

/**
 * Class with common methods regarding Excel population. By using this class, we are sure that
 * every Excel doc. will look the same with the others.
 */
public final class ExcelCommons {
    public static final String infoSheetName = "General";

    private ExcelCommons() {

    }

    public static CellStyle generateHeaderStyle(Workbook workbook) {
        Font font = generateHeaderFont(workbook);
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);
        return style;
    }

    private static Font generateHeaderFont(Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        font.setItalic(false);
        return font;
    }

    public static void createCellWithValue(Row row, int cellNr, Double value) {
        Cell cell = row.createCell(cellNr);
        cell.setCellValue(value);
    }

    public static void createCellWithValue(Row row, int cellNr, String value) {
        Cell cell = row.createCell(cellNr);
        cell.setCellValue(value);
    }

    public static void createCellWithValue(Row row, int cellNr, int value) {
        Cell cell = row.createCell(cellNr);
        cell.setCellValue(value);
    }

    public static void createCellWithFormula(Row row, int cellNr, String formula) {
        Cell cell = row.createCell(cellNr);
        cell.setCellFormula(formula);
    }

    /**
     * Method used for optional values that can be null. Replacing null with "-"
     */
    public static void createCellWithNullCheck(Row row, int cellNr, Double value) {
        Cell cell = row.createCell(cellNr);
        if (isNull(value)) {
            cell.setCellValue("-");
            return;
        }
        cell.setCellValue(value);
    }

    public static void createCellWithNullCheckCommons(Row firstRow, Row secondRow, int cellNr, Double value) {
        createCellWithNullCheck(firstRow, cellNr, value);
        createCellWithNullCheck(secondRow, cellNr, value);
    }

    public static Cell createCellWithStyle(Row row, int cellNumber, CellStyle cellStyle) {
        Cell cell = row.createCell(cellNumber);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    public static void createCellWithStyleAndValue(Row row, int cellNumber, CellStyle cellStyle, String value) {
        Cell cell = createCellWithStyle(row, cellNumber, cellStyle);
        cell.setCellValue(value);
    }

    /**
     * Method to create an empty cell on a specific position in the given row.
     * The simple statement could be used instead of calling the method, but
     * the method name has a clear intend.
     */
    public static void createEmptyColumn(Row row, int cellNr) {
        row.createCell(cellNr);
    }

    public static void createEmptyRow(int rowNum, Sheet sheet) {
        sheet.createRow(rowNum);
    }
}
