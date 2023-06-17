package com.batchservice;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class CustomerFieldSetMapper implements FieldSetMapper<Customer> {
    @Override
    public Customer mapFieldSet(FieldSet fieldSet) {
        Customer customer = new Customer();
        customer.setId(fieldSet.readInt(0));
        customer.setFirstName(fieldSet.readString(1));
        customer.setLastName(fieldSet.readString(2));
        customer.setEmail(fieldSet.readString(3));
        customer.setGender(fieldSet.readString(4));
        customer.setContact(fieldSet.readString(5));
        customer.setCountry(fieldSet.readString(6));
        customer.setDob(fieldSet.readString(7));
        return customer;
    }
}
