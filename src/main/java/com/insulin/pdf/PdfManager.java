package com.insulin.pdf;

import com.insulin.model.form.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

public class PdfManager {
    private final Document pdfDocument;
    private final Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private final Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private final Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    public PdfManager() throws DocumentException {
        this.pdfDocument = new Document();
        PdfWriter.getInstance(pdfDocument, byteArrayOutputStream);
        this.pdfDocument.open();
        addMetaData();
    }

    private void addMetaData() {
        pdfDocument.addTitle("Index Results");
        pdfDocument.addSubject("Results");
        pdfDocument.addKeywords("Index results, Glucose, Insulin");
        pdfDocument.addAuthor("InsulinX");
        pdfDocument.addCreator("InsulinX");
    }

    public void addTitleAndResult(IndexSender indexSender) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLines(paragraph, 1);
        paragraph.add(new Paragraph("Index result", titleFont));
        addEmptyLines(paragraph, 1);
        String[] result = indexSender.getResult().split("\\|");
        paragraph.add(new Paragraph("Chart result: " + result[0], redFont));
        paragraph.add(new Paragraph("Index result: " + result[1], redFont));
        addEmptyLines(paragraph, 1);
        paragraph.add(new Paragraph("The content of this PDF document is the same as the result page that you can find inside the application. " +
                "We displayed the inserted data into a tabular format, followed by the two charts (one for glucose and one for insulin) and finally, one can " +
                "see the results of the selected indexes.", smallBold));
        addEmptyLines(paragraph, 1);
        paragraph.add(new Paragraph("Please do not take the results here as granted. If the results indicates any kind of problem (pre-diabetes or diabetes)," +
                "please consult a specialised medical person in order to establish a correct diagnosis.", smallBold));
        addEmptyLines(paragraph, 2);
        pdfDocument.add(paragraph);
    }

    public void addGlucoseTable(GlucoseMandatory glucoseMandatory) throws DocumentException {
        createGenericParagraph("Glucose Data:");

        PdfPTable table = new PdfPTable(5);
        createHeader(table, "Placeholder");
        createHeader(table, "Fasting Glucose");
        createHeader(table, "Glucose 30");
        createHeader(table, "Glucose 60");
        createHeader(table, "Glucose 120");
        table.setHeaderRows(1);

        table.addCell(glucoseMandatory.getGlucosePlaceholder());
        table.addCell(glucoseMandatory.getFastingGlucose() + "");
        table.addCell(glucoseMandatory.getGlucoseThree() + "");
        table.addCell(glucoseMandatory.getGlucoseSix() + "");
        table.addCell(glucoseMandatory.getGlucoseOneTwenty() + "");
        Paragraph p = new Paragraph();
        addEmptyLines(p, 1);

        pdfDocument.add(table);
        pdfDocument.add(p);
    }

    public void addInsulinData(InsulinMandatory insulinMandatory) throws DocumentException {
        createGenericParagraph("Insulin Data:");

        PdfPTable table = new PdfPTable(5);
        createHeader(table, "Placeholder");
        createHeader(table, "Fasting Insulin");
        createHeader(table, "Insulin 30");
        createHeader(table, "Insulin 60");
        createHeader(table, "Insulin 120");
        table.setHeaderRows(1);

        table.addCell(insulinMandatory.getInsulinPlaceholder());
        table.addCell(insulinMandatory.getFastingInsulin() + "");
        table.addCell(insulinMandatory.getInsulinThree() + "");
        table.addCell(insulinMandatory.getInsulinSix() + "");
        table.addCell(insulinMandatory.getInsulinOneTwenty() + "");
        Paragraph p = new Paragraph();
        addEmptyLines(p, 1);

        pdfDocument.add(table);
        pdfDocument.add(p);
    }

    public void addOptionalData(OptionalIndexInformation optionalInformation) throws DocumentException {
        createGenericParagraph("Optional Data:");

        PdfPTable table = new PdfPTable(6);
        createHeader(table, "Height");
        createHeader(table, "Weight");
        createHeader(table, "HDL");
        createHeader(table, "NEFA");
        createHeader(table, "Th. globulin");
        createHeader(table, "Triglyceride");
        table.setHeaderRows(1);

        table.addCell(getValueOrLine(optionalInformation.getHeight()));
        table.addCell(getValueOrLine(optionalInformation.getWeight()));
        table.addCell(getValueOrLine(optionalInformation.getHdl()));
        table.addCell(getValueOrLine(optionalInformation.getNefa()));
        table.addCell(getValueOrLine(optionalInformation.getThyroglobulin()));
        table.addCell(getValueOrLine(optionalInformation.getTriglyceride()));

        pdfDocument.add(table);
    }

    public void addImage(String image, String caption) throws IOException, DocumentException {
        image = image.replace("data:image/png;base64,", "").replace("\n", "").replace(" ", "+");
        image = image.trim();
        byte[] result = Base64.getMimeDecoder().decode(image);
        Image img = Image.getInstance(result);
        img.scaleToFit(500, 600);
        createGenericParagraph(caption);
        pdfDocument.add(img);
        Paragraph p = new Paragraph();
        addEmptyLines(p, 2);
        pdfDocument.add(p);
    }

    public void addIndexResult(IndexSender sender) throws DocumentException {
        createGenericParagraph("Index results: ");
        PdfPTable table = new PdfPTable(4);
        createHeader(table, "Index name");
        createHeader(table, "Result");
        createHeader(table, "Interpretation");
        createHeader(table, "Normal values");
        table.setHeaderRows(1);

        sender.getResults().forEach((name, result) -> {
            table.addCell(capitalizeFully(name));
            table.addCell(result.getResult() + "");
            table.addCell(result.getMessage());
            table.addCell(result.getNormalRange());
        });
        pdfDocument.add(table);
    }

    private void createGenericParagraph(String description) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLines(paragraph, 1);
        paragraph.add(new Paragraph(description, smallBold));
        addEmptyLines(paragraph, 1);
        pdfDocument.add(paragraph);
    }

    public byte[] returnPdf() {
        this.pdfDocument.close();
        return byteArrayOutputStream.toByteArray();
    }

    public void createNewPage() {
        pdfDocument.newPage();
    }

    private void createHeader(PdfPTable table, String header) {
        PdfPCell cell = new PdfPCell(new Phrase(header));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addEmptyLines(Paragraph paragraph, int lines) {
        for (int i = 0; i < lines; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private String getValueOrLine(Double value) {
        if (value == null) {
            return "-";
        }
        return value.toString();
    }
}
