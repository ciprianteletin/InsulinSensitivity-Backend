package com.insulin.service.implementation;

import com.insulin.excel.ExcelManager;
import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.service.abstraction.ExcelService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Override
    public byte[] exportResponseExcel(MandatoryInsulinInformation mandatoryInformation, IndexSender sender)
            throws IOException {
        ExcelManager excelManager = new ExcelManager("insulin.xlsx");
        excelManager.addToGeneralPage(mandatoryInformation);
        excelManager.addFormulaPage(sender, "Indexes.xlsx");
        // excelManager.exportAsFile(LOCAL_PATH);
        return excelManager.getExcelDocument();
    }

    @Override
    public void constructExcelDocument(ExcelManager excelManager, MandatoryInsulinInformation mandatoryInformation,
                                       IndexSender sender, String creationDate) {
        excelManager.addToGeneralPage(mandatoryInformation);
        excelManager.addFormulaPage(sender, creationDate);
        excelManager.increaseInfoId();
    }
}
