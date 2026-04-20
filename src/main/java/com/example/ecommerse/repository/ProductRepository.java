package com.example.ecommerse.repository;

import com.example.ecommerse.model.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductRepository {

    private final AtomicLong sequence = new AtomicLong(3);
    private final Map<Long, Product> products = new ConcurrentHashMap<>();

    @PostConstruct
    public void seedData() {
        save(new Product(1L, "Wireless Mouse", "Ergonomic wireless mouse", "Accessories", new BigDecimal("24.99"), 45));
        save(new Product(2L, "Mechanical Keyboard", "RGB mechanical keyboard", "Accessories", new BigDecimal("79.99"), 20));
        save(new Product(3L, "USB-C Hub", "7-in-1 hub for laptops", "Adapters", new BigDecimal("49.50"), 35));
    }

    public List<Product> findAll() {
        return products.values().stream()
                .sorted(Comparator.comparing(Product::id))
                .toList();
    }

    public Product findById(Long id) {
        return products.get(id);
    }

    public Product save(Product product) {
        products.put(product.id(), product);
        return product;
    }

    public Product create(Product product) {
        long id = sequence.incrementAndGet();
        Product created = new Product(id, product.name(), product.description(), product.category(), product.price(), product.stock());
        products.put(id, created);
        return created;
    }

    public void replace(Product product) {
        products.put(product.id(), product);
    }
}
