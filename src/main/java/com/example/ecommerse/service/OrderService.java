package com.example.ecommerse.service;

import com.example.ecommerse.dto.OrderItemRequest;
import com.example.ecommerse.dto.OrderRequest;
import com.example.ecommerse.exception.InsufficientStockException;
import com.example.ecommerse.model.Order;
import com.example.ecommerse.model.OrderItem;
import com.example.ecommerse.model.Product;
import com.example.ecommerse.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;

    public OrderService(ProductService productService, OrderRepository orderRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(String username, OrderRequest request) {
        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        Map<Long, Integer> requestedQuantities = new HashMap<>();

        for (OrderItemRequest itemRequest : request.items()) {
            requestedQuantities.merge(itemRequest.productId(), itemRequest.quantity(), Integer::sum);
        }

        for (Map.Entry<Long, Integer> entry : requestedQuantities.entrySet()) {
            Product product = productService.getProduct(entry.getKey());
            if (product.stock() < entry.getValue()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.name());
            }
        }

        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productService.getProduct(itemRequest.productId());
            BigDecimal lineTotal = product.price().multiply(BigDecimal.valueOf(itemRequest.quantity()));
            items.add(new OrderItem(product.id(), product.name(), itemRequest.quantity(), product.price(), lineTotal));
            total = total.add(lineTotal);
        }

        for (OrderItemRequest itemRequest : request.items()) {
            productService.reduceStock(itemRequest.productId(), itemRequest.quantity());
        }

        Order order = new Order(null, username, request.customerEmail(), "CREATED", Instant.now(), items, total);
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersForUser(String username) {
        return orderRepository.findByCustomerUsername(username);
    }
}
