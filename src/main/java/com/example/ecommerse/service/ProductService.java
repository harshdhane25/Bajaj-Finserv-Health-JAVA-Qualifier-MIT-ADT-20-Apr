package com.example.ecommerse.service;

import com.example.ecommerse.dto.ProductRequest;
import com.example.ecommerse.exception.NotFoundException;
import com.example.ecommerse.model.Product;
import com.example.ecommerse.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new NotFoundException("Product not found: " + id);
        }
        return product;
    }

    public Product createProduct(ProductRequest request) {
        return productRepository.create(new Product(
                null,
                request.name(),
                request.description(),
                request.category(),
                request.price(),
                request.stock()
        ));
    }

    public void reduceStock(Long productId, int quantity) {
        Product product = getProduct(productId);
        productRepository.replace(new Product(
                product.id(),
                product.name(),
                product.description(),
                product.category(),
                product.price(),
                product.stock() - quantity
        ));
    }
}
