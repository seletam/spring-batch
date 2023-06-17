package com.batchservice;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class InvoiceMapper implements Function<Invoice, Invoice> {

    @Override
    public Invoice apply(Invoice invoice) {
        return Invoice.builder()
                .customerId(invoice.getCustomerId())
                .total(invoice.getTotal())
                .id(invoice.getId())
                .amount(invoice.getAmount())
                .referenceNumber(invoice.getReferenceNumber())
                .build();
    }
}