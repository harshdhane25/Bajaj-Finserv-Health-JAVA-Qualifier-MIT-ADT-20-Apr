package com.example.ecommerse.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank @Size(max = 80) String name,
        @NotBlank @Size(max = 255) String description,
        @NotBlank @Size(max = 40) String category,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        @Min(0) int stock
) {
}
