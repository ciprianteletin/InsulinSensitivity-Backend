package com.insulin.service.abstraction;

import com.insulin.excel.ExcelManager;
import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryIndexInformation;

import java.io.IOException;

public interface ExcelService {
    byte[] exportResponseExcel(MandatoryIndexInformation mandatoryInformation, IndexSender sender) throws IOException;

    void constructExcelDocument(ExcelManager excelManager, MandatoryIndexInformation mandatoryInformation,
                                IndexSender sender, String creationDate);
}
