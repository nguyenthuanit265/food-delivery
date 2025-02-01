package com.myapp.service;

import com.myapp.model.dto.OrderRequest;
import com.myapp.model.entity.Order;
import com.myapp.model.entity.OrderStatus;
import com.myapp.model.entity.User;
import com.myapp.repository.OrderRepository;
import com.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final KafkaTemplate<String, Order> kafkaTemplate;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, KafkaTemplate<String, Order> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(OrderRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseGet(() -> createNewUser(request));

        Order order = new Order();
        order.setCustomerId(user.getId());
//        order.setRestaurantId(request.getRestaurantId());
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setSpecialInstructions(request.getSpecialInstructions());
        Order savedOrder = orderRepository.save(order);

        // Send to Kafka
        kafkaTemplate.send("new-orders", savedOrder);
        return savedOrder;
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }


    private User createNewUser(OrderRequest request) {
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPasswordHash("temporary"); // In real app, handle proper password
        newUser.setRole("CUSTOMER");
        newUser.setCreatedAt(LocalDateTime.now());
        return userRepository.save(newUser);
    }
}
