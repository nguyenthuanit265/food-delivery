package com.myapp.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaPartitionAssigner {

    @Value("${app.instance.id}")
    private String instanceId;

    private final Logger log = LoggerFactory.getLogger(KafkaPartitionAssigner.class);

    @Bean
    public String[] assignedPartitions() {
        // Example: instance-1 -> [0,1], instance-2 -> [2,3]
        Map<String, List<Integer>> assignmentMap = new HashMap<>();
        assignmentMap.put("instance-0", Arrays.asList(0));
        assignmentMap.put("instance-1", Arrays.asList(1));
        assignmentMap.put("instance-2", Arrays.asList(2));

        List<Integer> partitions = assignmentMap.get(instanceId);
        if (partitions == null || partitions.isEmpty()) {
            log.error("No partition assignment found for instance {}", instanceId);
            return new String[0];
        }

        log.info("Instance = {} assigning partition = {}", instanceId, partitions);
        return partitions.stream()
                .map(String::valueOf)
                .toArray(String[]::new);
    }
}
