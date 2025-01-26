package com.myapp.model.entity;

public enum OrderStatus {
    NEW,                // Order just created
    CONFIRMED,          // Restaurant accepted
    PREPARING,          // Restaurant preparing food
    READY_FOR_PICKUP,   // Food ready for driver
    PICKED_UP,          // Driver picked up food
    IN_DELIVERY,        // Driver en route
    DELIVERED,          // Successfully delivered
    CANCELLED,          // Order cancelled
    REJECTED            // Restaurant rejected
}
