package com.invoice.project.dto;

import java.util.List;

public class InvoiceRequestDTO {
    public SellerDTO seller;
    public ClientDTO client;
    public MetaDTO meta;
    public List<ItemDTO> items;

    public static class SellerDTO {
        public String companyName;
        public String companyAddress;
        public String contactNumber;
        public String email;
        public String taxId;
        public String logoUrl;
    }

    public static class ClientDTO {
        public String name;
        public String address;
        public String contactNumber;
        public String email;
        public String taxId;
    }

    public static class MetaDTO {
        public String invoiceNumber;
        public String invoiceDate;
        public String dueDate;
        public String paymentTerms;
        public String currency;
        public double taxRatePercent;
        public double discountPercent;
        public double discountAmount;
        public double subtotal;
        public double tax;
        public double discount;
        public double grandTotal;
        public String paymentInstructions;
        public String terms;
        public String thanks;
    }

    public static class ItemDTO {
        public String description;
        public int quantity;
        public double unitPrice;
        public double lineTotal;
    }
}
