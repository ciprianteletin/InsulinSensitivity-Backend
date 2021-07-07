package com.insulin.service.implementation;

import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryIndexInformation;
import com.insulin.pdf.PdfManager;
import com.insulin.service.abstraction.PdfService;
import com.insulin.utils.model.Pair;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PdfServiceImpl implements PdfService {

    @Override
    public byte[] getPDF(String glucoseImg, String insulinImg, Pair<IndexSender, MandatoryIndexInformation> pair)
            throws IOException, DocumentException {
        IndexSender sender = pair.getFirst();
        MandatoryIndexInformation mandatoryInformation = pair.getSecond();
        PdfManager pdfManager = new PdfManager();
        createFirstPage(pdfManager, sender, mandatoryInformation);
        createSecondPage(pdfManager, sender, glucoseImg, insulinImg);

        return pdfManager.returnPdf();
    }

    private void createFirstPage(PdfManager pdfManager, IndexSender sender, MandatoryIndexInformation mandatoryInformation)
            throws DocumentException {
        pdfManager.addTitleAndResult(sender);
        pdfManager.addGlucoseTable(mandatoryInformation.getGlucoseMandatory());
        pdfManager.addInsulinData(mandatoryInformation.getInsulinMandatory());
        pdfManager.addOptionalData(mandatoryInformation.getOptionalInformation());
        pdfManager.createNewPage();
    }

    private void createSecondPage(PdfManager pdfManager, IndexSender sender, String glucoseImg, String insulinImg)
            throws IOException, DocumentException {
        pdfManager.addImage(glucoseImg, "Glucose chart:");
        pdfManager.addImage(insulinImg, "Insulin chart:");
        pdfManager.createNewPage();
        pdfManager.addIndexResult(sender);
    }
}
