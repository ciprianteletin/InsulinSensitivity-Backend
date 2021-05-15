package com.insulin.excel.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to keep the evidence of the numbers of rows in a sheet page. It will keep track
 * of the state without changing attributes of a class, making it a cleaner approach.
 */
public class RowTracker {
    private final Map<String, Integer> tracker;

    public RowTracker() {
        tracker = new HashMap<>();
    }

    public void addNewRecord(String sheetName, int startingRow) {
        tracker.putIfAbsent(sheetName, startingRow);
    }

    public int getAndUpdate(String sheetName) {
        int currentRow = tracker.get(sheetName);
        this.tracker.put(sheetName, currentRow + 1);
        return currentRow;
    }
}
