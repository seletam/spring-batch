package com.batchservice;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;

import static java.lang.String.format;

@Component
public class CustomerProcessor implements ItemProcessor<Customer, Invoice> {


    private final CustomerToInvoiceMapper customerToInvoiceMapper;

    public CustomerProcessor(CustomerToInvoiceMapper customerToInvoiceMapper) {
        this.customerToInvoiceMapper = customerToInvoiceMapper;
    }

    @Override
    public Invoice process(Customer customer) {
        return customerToInvoiceMapper.apply(customer);
    }

}
