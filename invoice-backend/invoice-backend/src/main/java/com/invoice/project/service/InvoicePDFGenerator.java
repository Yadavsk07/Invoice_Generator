package com.invoice.project.service;

import com.invoice.project.entity.Invoice;
import com.invoice.project.entity.InvoiceItem;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class InvoicePDFGenerator {

    public byte[] generateInvoicePdf(Invoice invoice) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 72, 36);
            PdfWriter.getInstance(document, out);
            document.open();

            // === Title ===
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLACK);
            Paragraph title = new Paragraph("INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // === Seller & Customer Info Table ===
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1, 1});

//            document.add(new Paragraph("Seller: " + invoice.getSellerName()));
//            document.add(new Paragraph("Email: " + invoice.getSellerEmail()));
//            document.add(new Paragraph("Address: " + invoice.getSellerAddress()));
//            document.add(new Paragraph("Contact: " + invoice.getSellerContact()));
//            document.add(Chunk.NEWLINE);

            PdfPCell sellerCell = new PdfPCell();
            sellerCell.setBorder(Rectangle.NO_BORDER);
            sellerCell.addElement(new Paragraph("Seller:", boldFont()));
            sellerCell.addElement(new Paragraph(invoice.getSellerName()));
            sellerCell.addElement(new Paragraph("Address: " + invoice.getSellerAddress()));
            sellerCell.addElement(new Paragraph("Email: " + invoice.getSellerEmail()));
            sellerCell.addElement(new Paragraph("Phone: " + invoice.getSellerContact()));

            PdfPCell customerCell = new PdfPCell();
            customerCell.setBorder(Rectangle.NO_BORDER);
            customerCell.addElement(new Paragraph("Bill To:", boldFont()));
            customerCell.addElement(new Paragraph(invoice.getCustomerName()));
            customerCell.addElement(new Paragraph(invoice.getCustomerEmail()));
            customerCell.addElement(new Paragraph("Invoice Date: " + invoice.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));

            infoTable.addCell(sellerCell);
            infoTable.addCell(customerCell);
            document.add(infoTable);
            document.add(Chunk.NEWLINE);

            // === Items Table ===
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 1, 2, 2});

            addTableHeader(table, "Description", "Qty", "Unit Price", "Total");

            boolean alternate = false;
            for (InvoiceItem item : invoice.getItems()) {
                BaseColor bgColor = alternate ? new BaseColor(245, 245, 245) : BaseColor.WHITE;
                alternate = !alternate;
                addTableCell(table, item.getDescription(), bgColor);
                addTableCell(table, String.valueOf(item.getQuantity()), bgColor);
                addTableCell(table, String.format("%.2f", item.getUnitPrice()), bgColor);
                addTableCell(table, String.format("%.2f", item.getPrice()), bgColor);
            }
            document.add(table);
            document.add(Chunk.NEWLINE);

            // === Totals Section ===
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(40);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.setWidths(new float[]{2, 2});

            addTotalRow(totalTable, "Subtotal:", String.format("%.2f", invoice.getItems().stream().mapToDouble(InvoiceItem::getPrice).sum()));
            addTotalRow(totalTable, "Tax:", String.format("%.2f", invoice.getTax()));
            addTotalRow(totalTable, "Discount:", String.format("%.2f", invoice.getDiscount()));
            addTotalRowBold(totalTable, "Grand Total:", String.format("%.2f", invoice.getTotalAmount()));

            document.add(totalTable);
            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating invoice PDF", e);
        }
    }

    private Font boldFont() {
        return new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, boldFont()));
            cell.setBackgroundColor(new BaseColor(230, 230, 250));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    private void addTableCell(PdfPTable table, String text, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addTotalRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label));
        labelCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value));
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(valueCell);
    }

    private void addTotalRowBold(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, boldFont()));
        labelCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, boldFont()));
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(valueCell);
    }
}
