package com.myapp.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_events")
public class ProcessedEvent {
    @Id
    private String eventId;
    private LocalDateTime processedAt;

    // Using natural key for eventId: topic-partition-offset
    public static String generateEventId(String topic, int partition, long offset) {
        return String.format("%s-%d-%d", topic, partition, offset);
    }
}
