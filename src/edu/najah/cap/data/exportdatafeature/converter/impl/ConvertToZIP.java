package edu.najah.cap.data.exportdatafeature.converter.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import edu.najah.cap.data.exportdatafeature.converter.intf.IConverter;
import edu.najah.cap.exceptions.InvalidUserDataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ConvertToZIP implements IConverter {
    private static final Logger logger = LogManager.getLogger(ConvertToZIP.class);
    @Override
    public byte[] convert(List<String> pdfDataList) throws InvalidUserDataException {
        String[] nameOfFiles = {"User_Details","Payment_Details"};
        try (ByteArrayOutputStream zipByteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(zipByteArrayOutputStream)) {
            for (int i = 0; i < pdfDataList.size(); i++) {
                String pdfData = pdfDataList.get(i);
                ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
                Document document = new Document();
                PdfWriter.getInstance(document, pdfOutputStream);
                document.open();
                String[] lines = pdfData.split("\n");
                for (String line : lines) {
                    document.add(new Paragraph(line));
                }
                document.close();
                ZipEntry zipEntry = new ZipEntry(nameOfFiles[i]  + ".pdf");
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(pdfOutputStream.toByteArray());
                zipOutputStream.closeEntry();
            }
            zipOutputStream.finish();
            logger.info("Make the ZIP file for data user . ");
            return zipByteArrayOutputStream.toByteArray();
        } catch (DocumentException | IOException e) {
            logger.error("Error in the convert data file to ZIP file in calls ConvertToZIP .");
            throw new InvalidUserDataException("No data for user to ZIP it .");
        }
    }
}



