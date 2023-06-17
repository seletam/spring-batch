package com.batchservice;

import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomerFileItemReader implements ItemReader<Customer> {

    private final Properties properties;

    public CustomerFileItemReader(Properties properties) {
        this.properties = properties;
    }

    private FlatFileItemReader<Customer> customerReader() {
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        String filePath = properties.getFilePath();
        itemReader.setResource(new FileSystemResource(filePath));
        itemReader.setLineMapper(customerMapper());
        itemReader.setName("customerReader");
        itemReader.setLinesToSkip(1);
        itemReader.setStrict(false);

        return itemReader;
    }

    private DefaultLineMapper<Customer> customerMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer(","));
        lineMapper.setFieldSetMapper(new CustomerFieldSetMapper());
        lineMapper.afterPropertiesSet();
        return lineMapper;
    }

    @Override
    public Customer read() throws Exception {
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        String filePath = properties.getFilePath();
        itemReader.setResource(new FileSystemResource(filePath));
        itemReader.setLineMapper(customerMapper());
        itemReader.setName("customerReader");
        itemReader.setLinesToSkip(1);
        itemReader.setStrict(false);
        itemReader.open(new ExecutionContext());
        Customer customer = null;
        while(itemReader.read() != null) {
            customer = Customer.builder().id(Objects.requireNonNull(itemReader.read()).getId()).build();
        }
        itemReader.close();

        return customer;
    }
}
