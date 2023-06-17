package com.batchservice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CustomerItemWriter customerWriter;

    private final CustomerFileItemReader customerFileItemReader;

    private final CustomerProcessor customerProcessor;

    private final CustomerPartitioner customerPartitioner;


    public BatchConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager, CustomerItemWriter customerWriter, CustomerFileItemReader customerFileItemReader, CustomerProcessor customerProcessor, CustomerPartitioner customerPartitioner) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.customerWriter = customerWriter;
        this.customerFileItemReader = customerFileItemReader;
        this.customerProcessor = customerProcessor;
        this.customerPartitioner = customerPartitioner;
    }

    @Bean
    public Step slaveStep() throws IOException {
        long chunkSize = Files.lines(new ClassPathResource("customer_batch.csv").getFile().toPath()).count() - 1;
        int size = (int) chunkSize / 4;
        System.out.println("chunkSize = " + size);
        return new StepBuilder("slaveStep", jobRepository)
                .<Customer, Invoice>chunk(size, transactionManager)
                .reader(customerFileItemReader)
                .processor(customerProcessor)
                .writer(customerWriter)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step masterStep() throws IOException {
        return new StepBuilder("masterStep", jobRepository)
                .partitioner(slaveStep().getName(), customerPartitioner)
                .partitionHandler(partitionHandler())
                .build();
    }

    @Bean
    public Job job() throws IOException {
        return new JobBuilder("customerJob", jobRepository)
                .flow(masterStep())
                .end()
                .build();
    }

    @Bean
    public AsyncTaskExecutor taskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }

    @Bean
    public PartitionHandler partitionHandler() throws IOException {
        TaskExecutorPartitionHandler retVal = new TaskExecutorPartitionHandler();
        retVal.setTaskExecutor(taskExecutor());
        retVal.setStep(slaveStep());
        retVal.setGridSize(10);
        return retVal;
    }
}
