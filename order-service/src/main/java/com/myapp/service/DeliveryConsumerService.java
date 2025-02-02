package com.myapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myapp.config.KafkaConsumerConfig;
import com.myapp.model.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeliveryConsumerService {

    private final Logger log = LoggerFactory.getLogger(DeliveryConsumerService.class);
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @KafkaListener(
            topicPartitions = {
                    @TopicPartition(
                            topic = "new-orders",
                            partitionOffsets = {
                                    @PartitionOffset(partition = "0",
                                            initialOffset = "${spring.kafka.consumer.groups.delivery.partitions.0.offset}"),
                                    @PartitionOffset(partition = "1",
                                            initialOffset = "${spring.kafka.consumer.groups.delivery.partitions.1.offset}"),
                                    @PartitionOffset(partition = "2",
                                            initialOffset = "${spring.kafka.consumer.groups.delivery.partitions.2.offset}")
                            }
                    )
            },
            groupId = "${spring.kafka.consumer.groups.delivery.id}"
    )
    public void processDelivery(@Payload Map<String, Object> payload,
                                @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                                @Header(KafkaHeaders.OFFSET) long offset,
                                Acknowledgment ack) {
        try {
            Order order = objectMapper.convertValue(payload, Order.class);
            log.info("processDelivery - Received order: {} from partition: {} at offset: {}",
                    order.getId(), partition, offset);

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing order", e);
            throw e;
        }
    }
}
