package com.example.microservice_norifications.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderEvent {
    private Long orderId;
    private Long userId;
    private String status;
    private LocalDateTime timestamp;
    private String message;
}