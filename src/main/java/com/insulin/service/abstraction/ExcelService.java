package com.insulin.service.abstraction;

import com.insulin.excel.ExcelManager;
import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryInsulinInformation;

import java.io.IOException;

public interface ExcelService {
    byte[] exportResponseExcel(MandatoryInsulinInformation mandatoryInformation, IndexSender sender) throws IOException;

    void constructExcelDocument(ExcelManager excelManager, MandatoryInsulinInformation mandatoryInformation,
                                IndexSender sender, String creationDate);
}
