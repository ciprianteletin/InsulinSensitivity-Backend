package com.insulin.interfaces;

/**
 * Interface used in the process of creating Excel documents. Each index will implement this interface (by implementing FormulaMaker)
 * and will return the expected Excel formula which will be written in a particular cell.
 */
public interface ExcelFormula {
    String generateExcelFormula(int infoId);
}
