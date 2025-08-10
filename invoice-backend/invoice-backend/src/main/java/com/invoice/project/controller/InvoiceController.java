package com.invoice.project.controller;

import com.invoice.project.dto.InvoiceRequestDTO;
import com.invoice.project.entity.Invoice;
import com.invoice.project.entity.InvoiceItem;
import com.invoice.project.service.InvoicePDFGenerator;
import com.invoice.project.service.InvoiceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:5173")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoicePDFGenerator pdfGenerator;

    public InvoiceController(InvoiceService invoiceService, InvoicePDFGenerator pdfGenerator) {
        this.invoiceService = invoiceService;
        this.pdfGenerator = pdfGenerator;
    }

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createInvoice(@RequestBody InvoiceRequestDTO dto) {
        Invoice invoice = new Invoice();

        // ===== Seller Info =====
        invoice.setSellerName(dto.seller.companyName);
        invoice.setSellerAddress(dto.seller.companyAddress);
        invoice.setSellerContact(dto.seller.contactNumber);
        invoice.setSellerEmail(dto.seller.email);

        // ===== Customer Info =====
        invoice.setCustomerName(dto.client.name);
        invoice.setCustomerEmail(dto.client.email);
        invoice.setCustomerAddress(dto.client.address);
        invoice.setCustomerContact(dto.client.contactNumber);

        // ===== Invoice Meta Info =====
        invoice.setInvoiceNumber(dto.meta.invoiceNumber);
        invoice.setDate(LocalDate.parse(dto.meta.invoiceDate));
        invoice.setSubtotal(dto.meta.subtotal);
        invoice.setTax(dto.meta.tax);
        invoice.setDiscount(dto.meta.discount);
        invoice.setTotalAmount(dto.meta.grandTotal);
        invoice.setPaymentTerms(dto.meta.paymentTerms);
        invoice.setPaymentInstructions(dto.meta.paymentInstructions);
        invoice.setNotes(dto.meta.terms);

        // ===== Items =====
        List<InvoiceItem> items = dto.items.stream().map(it -> {
            InvoiceItem item = new InvoiceItem();
            item.setDescription(it.description);
            item.setQuantity(it.quantity);
            item.setUnitPrice(it.unitPrice);
            item.setPrice(it.unitPrice * it.quantity);
            item.setInvoice(invoice);
            return item;
        }).toList();

        invoice.setItems(items);

        Invoice saved = invoiceService.saveInvoice(invoice);

        return ResponseEntity.created(URI.create("/api/invoices/" + saved.getId())).build();
    }

    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id)
                .map(invoice -> {
                    byte[] pdf = pdfGenerator.generateInvoicePdf(invoice);
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + id + ".pdf")
                            .contentType(MediaType.APPLICATION_PDF)
                            .body(pdf);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
