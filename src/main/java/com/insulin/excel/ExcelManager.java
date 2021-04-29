package com.insulin.excel;

import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryInsulinInformation;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class ExcelManager {
    private final Workbook workbook;
    private final InformationSheet informationSheet;
    private final String fileName;
    private final FormulaSheet formulaSheet;

    public ExcelManager(String fileName) {
        this.workbook = new XSSFWorkbook();
        this.fileName = fileName;
        this.informationSheet = new InformationSheet(workbook);
        this.formulaSheet = new FormulaSheet(workbook);
    }

    public void createGeneralPage(MandatoryInsulinInformation mandatoryInformation) {
        informationSheet.addMandatoryInformation(mandatoryInformation);
    }

    public void addFormulaPage(IndexSender sender) {
        int currentInformation = informationSheet.getCurrentInfoId();
        formulaSheet.setCurrentInformation(currentInformation);
        formulaSheet.addFormulaPage(sender);
    }

    private Sheet addSheet(String sheetName) {
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(6000);
        return sheet;
    }

    public byte[] getExcelDocument() throws IOException {
        String fileLocation = getFileLocation();
        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();

        File file = new File(fileLocation);
        return Files.readAllBytes(file.toPath());
    }

    /**
     * Method used for debugging exclusively, to check that the process works and that all data looks how it should.
     * Although it's not used, we left in there in case someone want to download the excel directly to a specific path.
     */
    public void exportAsFile(String path) throws IOException {
        path = path + "/" + fileName;
        FileOutputStream fileOutput = new FileOutputStream(path);
        workbook.write(fileOutput);
        workbook.close();
    }

    private String getFileLocation() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        return path.substring(0, path.length() - 1) + "src/main/resources/" + fileName;
    }
}
