package com.example.commande.service;

import com.example.commande.client.ProductClient;
import com.example.commande.entity.Order;
import com.example.commande.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${product.service.graphql.url}")
    private String productServiceGraphQLUrl;

    private final ProductClient productClient;

    public OrderService(ProductClient productClient) {
        this.productClient = productClient;
    }

    public Product fetchProduct(Long productId) {
        return productClient.getProductById(productId);
    }

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate, KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Order createOrder(Long productId, int quantity) {
        Order order = new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setStatus("CREATED");
        order.setOrderDate(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);


        kafkaTemplate.send("orders", "New order created: " + savedOrder.getId());

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
