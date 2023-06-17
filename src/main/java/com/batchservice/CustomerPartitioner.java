package com.batchservice;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomerPartitioner implements Partitioner {

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitions = new HashMap<>(gridSize);

        for (int i = 0; i < gridSize; i++) {
            ExecutionContext context1 = new ExecutionContext();
            context1.put("partitionNumber", i);
            System.out.println("current Thread = " + Thread.currentThread().isVirtual());
            partitions.put("partition" + i, context1);
        }

        return partitions;
    }
}
