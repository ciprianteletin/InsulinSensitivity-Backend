package com.insulin.service.abstraction;

import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.utils.model.Pair;
import com.itextpdf.text.DocumentException;

import java.io.IOException;

public interface PdfService {
    byte[] getPDF(String glucoseImg, String insulinImg, Pair<IndexSender, MandatoryInsulinInformation> pair) throws IOException, DocumentException;
}
