package com.example.microservice_commandes.service;

import com.example.microservice_commandes.client.ProductClient;
import com.example.microservice_commandes.model.Order;
import com.example.microservice_commandes.model.OrderStatus;
import com.example.microservice_commandes.repository.OrderRepository;
import com.example.microservice_produits.model.Product;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    @Retry(name = "productService", fallbackMethod = "createOrderFallback")
    public Order createOrder(Order order) {
        logger.info("Creating order for product: {}", order.getProductId());

        // Récupérer les informations du produit
        Product product = productClient.getProduct(order.getProductId());
        order.setProductName(product.getName());
        order.setUnitPrice(product.getPrice());
        order.setSubtotal(product.getPrice() * order.getQuantity());

        return orderRepository.save(order);
    }

    public Order createOrderFallback(Order order, Throwable throwable) {
        logger.error("Failed to retrieve product information. Using fallback method.", throwable);

        order.setProductName("Indisponible");
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    public Order getOrder(Long id) {
        return (Order) orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = getOrder(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}