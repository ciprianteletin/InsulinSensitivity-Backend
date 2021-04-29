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
        excelManager.createGeneralPage(mandatoryInformation);
        excelManager.addFormulaPage(sender);
        //excelManager.exportAsFile(LOCAL_PATH);
        return excelManager.getExcelDocument();
    }
}
