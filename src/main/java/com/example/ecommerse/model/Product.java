package com.example.ecommerse.model;

import java.math.BigDecimal;

public record Product(
        Long id,
        String name,
        String description,
        String category,
        BigDecimal price,
        int stock
) {
}
