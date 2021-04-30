package com.insulin.excel;

import com.insulin.excel.utils.FormulaSheetTracker;
import com.insulin.excel.utils.RowTracker;
import com.insulin.interfaces.ExcelFormula;
import com.insulin.model.form.Formula;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.IndexSender;
import org.apache.poi.ss.usermodel.*;

import java.util.Map;

import static com.insulin.excel.utils.ExcelCommons.*;

public class FormulaSheet {
    private final Workbook workbook;
    private int currentInformation;
    private final RowTracker tracker;
    private final Formula formula;
    private final FormulaSheetTracker sheetTracker;

    public FormulaSheet(Workbook workbook) {
        this.workbook = workbook;
        this.tracker = new RowTracker();
        this.formula = Formula.getInstance();
        this.sheetTracker = new FormulaSheetTracker();
    }

    public void setCurrentInformation(int currentInformation) {
        this.currentInformation = currentInformation;
    }

    public void addFormulaPage(IndexSender indexSender, String sheetName) {
        Sheet sheet = retrieveOrCreateSheet(sheetName);
        Map<String, IndexResult> results = indexSender.getResults();
        results.forEach((name, result) -> addIndexRow(name, result, sheet));
        for (int i = 0; i <= 5; i++) {
            sheet.autoSizeColumn(i);
        }
        createEmptyRow(tracker.getAndUpdate(sheetName), sheet);
    }

    private Sheet createSheet(String name) {
        Sheet sheet = workbook.createSheet(name);
        tracker.addNewRecord(name, 0);
        addPageHeader(sheet);
        return sheet;
    }

    private Sheet retrieveOrCreateSheet(String sheetName) {
        if (sheetTracker.containsSheet(sheetName)) {
            return sheetTracker.getSheetByName(sheetName);
        }
        Sheet sheet = createSheet(sheetName);
        sheetTracker.setSheet(sheetName, sheet);
        return sheet;
    }

    private void addPageHeader(Sheet sheet) {
        CellStyle cellStyle = generateHeaderStyle(workbook);
        int cellNr = 0;
        Row row = sheet.createRow(tracker.getAndUpdate(sheet.getSheetName()));
        createCellWithStyleAndValue(row, cellNr++, cellStyle, "Information id");
        createCellWithStyle(row, cellNr++, cellStyle);
        createCellWithStyleAndValue(row, cellNr++, cellStyle, "Index name");
        createCellWithStyleAndValue(row, cellNr++, cellStyle, "Result");
        createCellWithStyleAndValue(row, cellNr++, cellStyle, "Diagnosis");
        createCellWithStyleAndValue(row, cellNr, cellStyle, "Normal range");
    }

    private void addIndexRow(String name, IndexResult indexResult, Sheet sheet) {
        String sheetName = sheet.getSheetName();
        ExcelFormula excelFormula = formula.getExcelFormula(name);
        int cellNr = 0;
        Row row = sheet.createRow(this.tracker.getAndUpdate(sheetName));
        createCellWithValue(row, cellNr++, this.currentInformation);
        createEmptyColumn(row, cellNr++);
        System.out.println(excelFormula.generateExcelFormula(this.currentInformation));
        createCellWithValue(row, cellNr++, name);
        createCellWithFormula(row, cellNr++, excelFormula.generateExcelFormula(this.currentInformation));
        createCellWithValue(row, cellNr++, indexResult.getMessage());
        createCellWithValue(row, cellNr, indexResult.getNormalRange());
    }
}
