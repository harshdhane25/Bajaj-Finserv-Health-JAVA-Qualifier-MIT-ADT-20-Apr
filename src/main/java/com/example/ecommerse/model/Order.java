package com.example.ecommerse.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record Order(
        Long id,
        String customerUsername,
        String customerEmail,
        String status,
        Instant createdAt,
        List<OrderItem> items,
        BigDecimal totalAmount
) {
}
