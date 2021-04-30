package com.insulin.excel.utils;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.Map;

public class FormulaSheetTracker {
    private final Map<String, Sheet> creationSheet;

    public FormulaSheetTracker() {
        this.creationSheet = new HashMap<>();
    }

    public Sheet getSheetByName(String name) {
        return this.creationSheet.get(name);
    }

    public void setSheet(String name, Sheet sheet) {
        this.creationSheet.putIfAbsent(name, sheet);
    }

    public boolean containsSheet(String name) {
        return creationSheet.containsKey(name);
    }
}
