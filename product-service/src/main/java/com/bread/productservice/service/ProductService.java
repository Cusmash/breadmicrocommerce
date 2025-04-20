package com.bread.productservice.service;

import com.bread.productservice.model.Product;
import com.bread.productservice.repository.ProductRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Cacheable(value = "products_list")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }    

    @Cacheable(value = "product_by_id", key = "#id")
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    @CacheEvict(value = { "products_list", "product_by_id" }, allEntries = true)
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @CacheEvict(value = { "products_list", "product_by_id" }, allEntries = true)
    public Product updateProduct(String id, Product updatedProduct) {
        return productRepository.findById(id)
            .map(existingProduct -> {
                existingProduct.setName(updatedProduct.getName());
                existingProduct.setDescription(updatedProduct.getDescription());
                existingProduct.setPrice(updatedProduct.getPrice());
                existingProduct.setQuantity(updatedProduct.getQuantity());
                return productRepository.save(existingProduct);
            }).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @CacheEvict(value = { "products_list", "product_by_id" }, allEntries = true)
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }
}

