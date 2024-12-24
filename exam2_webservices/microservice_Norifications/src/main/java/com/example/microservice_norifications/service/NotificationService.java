package com.example.microservice_norifications.service;

import com.example.microservice_norifications.model.Notification;
import com.example.microservice_norifications.model.OrderEvent;
import com.example.microservice_norifications.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "order-events", groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderEvent(OrderEvent event) {
        logger.info("Received order event: {}", event);

        Notification notification = new Notification();
        notification.setUserId(event.getUserId());
        notification.setMessage(createNotificationMessage(event));
        notification.setRead(false);

        notificationRepository.save(notification);
        logger.info("Notification saved for user: {}", event.getUserId());
    }

    private String createNotificationMessage(OrderEvent event) {
        return switch (event.getStatus()) {
            case "CREATED" -> "Your order #" + event.getOrderId() + " has been created successfully.";
            case "CONFIRMED" -> "Your order #" + event.getOrderId() + " has been confirmed.";
            case "CANCELLED" -> "Your order #" + event.getOrderId() + " has been cancelled.";
            default -> "Order #" + event.getOrderId() + " status updated to: " + event.getStatus();
        };
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false);
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
}