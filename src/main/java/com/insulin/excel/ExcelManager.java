package com.insulin.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;

public class ExcelManager {
    private final Workbook workbook;

    public ExcelManager(String fileName) throws IOException {
        this.workbook = WorkbookFactory.create(new File(fileName));
    }

    private Sheet addSheet(String sheetName) {
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(6000);
        return sheet;
    }
}
