package com.invoice.project.service;

import com.invoice.project.entity.Invoice;
import com.invoice.project.entity.InvoiceItem;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class InvoicePDFService {

    public ByteArrayInputStream generatePDF(Invoice invoice) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Paragraph title = new Paragraph("Invoice #" + invoice.getId(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Customer: " + invoice.getCustomerName()));
            document.add(new Paragraph("Email: " + invoice.getCustomerEmail()));
            document.add(new Paragraph("Date: " + invoice.getDate()));
            document.add(new Paragraph(" ")); // Blank line

            // Table for items
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{4, 1, 2});

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Description"));
            table.addCell(hcell);
            hcell = new PdfPCell(new Phrase("Qty"));
            table.addCell(hcell);
            hcell = new PdfPCell(new Phrase("Price"));
            table.addCell(hcell);

            for (InvoiceItem item : invoice.getItems()) {
                table.addCell(item.getDescription());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(String.valueOf(item.getPrice()));
            }

            document.add(table);

            // Total
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total Amount: " + invoice.getTotalAmount()));

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
