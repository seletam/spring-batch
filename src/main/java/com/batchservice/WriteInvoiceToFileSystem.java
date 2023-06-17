package com.batchservice;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Component
public class WriteInvoiceToFileSystem implements Consumer<List<Invoice>> {

    public static final String FILE_PATH = "/home/keane/Downloads/test.csv";

    @Override
    public void accept(List<Invoice> invoices) {
        FileSystemResource fileSystemResource = new FileSystemResource(FILE_PATH);
        try (FileWriter writer = new FileWriter(fileSystemResource.getFile())) {
            populateFile.accept(invoices, writer);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final BiConsumer<List<Invoice>, FileWriter> populateFile = (invoices, fileWriter) -> invoices.forEach(invoice -> {
        try {
            fileWriter.write(invoice.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });

}
