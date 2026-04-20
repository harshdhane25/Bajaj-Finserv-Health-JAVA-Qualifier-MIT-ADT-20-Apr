package com.example.ecommerse.repository;

import com.example.ecommerse.model.Order;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class OrderRepository {

    private final AtomicLong sequence = new AtomicLong();
    private final Map<Long, Order> orders = new ConcurrentHashMap<>();

    public Order save(Order order) {
        long id = sequence.incrementAndGet();
        Order created = new Order(id, order.customerUsername(), order.customerEmail(), order.status(), order.createdAt(), order.items(), order.totalAmount());
        orders.put(id, created);
        return created;
    }

    public List<Order> findAll() {
        return orders.values().stream()
                .sorted(Comparator.comparing(Order::createdAt).reversed())
                .toList();
    }

    public List<Order> findByCustomerUsername(String username) {
        return orders.values().stream()
                .filter(order -> order.customerUsername().equals(username))
                .sorted(Comparator.comparing(Order::createdAt).reversed())
                .toList();
    }
}
