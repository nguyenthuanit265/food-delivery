package com.myapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "delivery_status")
public class DeliveryStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    private String status;
    private Point location;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}