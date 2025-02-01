package com.myapp.service;

import com.myapp.config.KafkaConsumerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryConsumerService {

    private final Logger log = LoggerFactory.getLogger(DeliveryConsumerService.class);

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
    public void processDelivery(ConsumerRecord<String, Object> record,
                                Acknowledgment ack) {
        try {
            log.info("Delivery Service - Received from partition {} offset {}",
                    record.partition(), record.offset());
            // Process delivery
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing delivery", e);
            throw e;
        }
    }
}
