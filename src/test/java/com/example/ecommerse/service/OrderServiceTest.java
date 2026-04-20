package com.example.ecommerse.service;

import com.example.ecommerse.dto.OrderItemRequest;
import com.example.ecommerse.dto.OrderRequest;
import com.example.ecommerse.model.Order;
import com.example.ecommerse.repository.OrderRepository;
import com.example.ecommerse.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderServiceTest {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        ProductRepository productRepository = new ProductRepository();
        productRepository.seedData();
        ProductService productService = new ProductService(productRepository);
        orderService = new OrderService(productService, new OrderRepository());
    }

    @Test
    void shouldCalculateOrderTotalAndKeepUsername() {
        OrderRequest request = new OrderRequest(
                "buyer@example.com",
                List.of(
                        new OrderItemRequest(1L, 2),
                        new OrderItemRequest(2L, 1)
                )
        );

        Order order = orderService.createOrder("tester", request);

        assertEquals("tester", order.customerUsername());
        assertEquals(new BigDecimal("129.97"), order.totalAmount());
        assertEquals(2, order.items().size());
    }
}
