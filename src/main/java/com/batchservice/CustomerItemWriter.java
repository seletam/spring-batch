package com.batchservice;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Component
public class CustomerItemWriter implements ItemWriter<Invoice> {

    private final InvoiceMapper invoiceMapper;
    private final WriteInvoiceToFileSystem writeInvoiceToFileSystem;

    public CustomerItemWriter(InvoiceMapper invoiceMapper, WriteInvoiceToFileSystem writeInvoiceToFileSystem) {
        this.invoiceMapper = invoiceMapper;
        this.writeInvoiceToFileSystem = writeInvoiceToFileSystem;
    }

    @Override
    public void write(Chunk<? extends Invoice> chunk) {
        writeInvoiceToFileSystem.accept(chunk.getItems()
                .stream()
                .map(invoiceMapper::apply)
                .toList());
    }
}