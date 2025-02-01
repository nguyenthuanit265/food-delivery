package com.myapp.service;

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

@Service
@RequiredArgsConstructor
public class OrderConsumerService {

    private final Logger log = LoggerFactory.getLogger(OrderConsumerService.class);


    @KafkaListener(
            topicPartitions = {
                    @TopicPartition(
                            topic = "new-orders",
                            partitionOffsets = {
                                    @PartitionOffset(partition = "0",
                                            initialOffset = "${spring.kafka.consumer.groups.order.partitions.0.offset}"),
                                    @PartitionOffset(partition = "1",
                                            initialOffset = "${spring.kafka.consumer.groups.order.partitions.1.offset}"),
                                    @PartitionOffset(partition = "2",
                                            initialOffset = "${spring.kafka.consumer.groups.order.partitions.2.offset}")
                            }
                    )
            },
            groupId = "${spring.kafka.consumer.groups.order.id}"
    )
//    public void processOrder(ConsumerRecord<String, Object> record,
//                             Acknowledgment ack) {
//        try {
//            log.info("Order Service - Received from partition {} offset {}",
//                    record.partition(), record.offset());
//            // Process order
//            ack.acknowledge();
//        } catch (Exception e) {
//            log.error("Error processing order", e);
//            throw e;
//        }
//    }
    public void processOrderWithHeaders(
            @Payload Order order,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
            Acknowledgment ack) {
        try {
            log.info("Received order: {} from partition: {} at offset: {}",
                    order.getId(), partition, offset);
            log.info("Topic: {}, Timestamp: {}", topic, timestamp);

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing order", e);
            throw e;
        }
    }
}
