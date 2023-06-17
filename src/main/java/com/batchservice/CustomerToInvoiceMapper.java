package com.batchservice;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.function.Function;

import static java.lang.String.format;

@Component
public class CustomerToInvoiceMapper implements Function<Customer, Invoice> {

    @Override
    public Invoice apply(Customer customer) {
        return Invoice.builder()
                .customerId(customer.getId())
                .referenceNumber(UUID.randomUUID())
                .id(new SecureRandom().nextInt(1, 1_000_000))
                .total(format("%.2f", new SecureRandom().nextDouble(0.0,
                        1_000_000_000.00)))
                .amount(format("%.2f", new SecureRandom().nextDouble(0.0,
                        1_000_000_000.00)))
                .build();
    }
}