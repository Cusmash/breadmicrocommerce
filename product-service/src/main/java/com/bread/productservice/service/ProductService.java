package com.bread.productservice.service;

import com.bread.productservice.dto.PagedResponseDTO;
import com.bread.productservice.model.Product;
import com.bread.productservice.model.ProductType;
import com.bread.productservice.repository.ProductRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                existingProduct.setImgUrl(updatedProduct.getImgUrl());
                existingProduct.setType(updatedProduct.getType());
                
                return productRepository.save(existingProduct);
            })
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @CacheEvict(value = { "products_list", "product_by_id" }, allEntries = true)
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public PagedResponseDTO<Product> getAllProductsPagedSorted(int page, int size, String sort) {
        Sort.Direction direction = sort.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "price"));
        var pageProducts = productRepository.findAll(pageable);
    
        return new PagedResponseDTO<>(
            pageProducts.getContent(),
            page,
            size,
            pageProducts.getTotalElements(),
            pageProducts.getTotalPages(),
            pageProducts.isLast()
        );
    }    

    public List<Product> searchProductsByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public List<Product> filterProducts(ProductType type, Double priceFrom, Double priceTo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
    
        if (type != null && priceFrom != null && priceTo != null) {
            return productRepository.findByTypeAndPriceBetween(type, priceFrom, priceTo, pageable);
        } else if (type != null) {
            return productRepository.findByType(type, pageable);
        } else if (priceFrom != null && priceTo != null) {
            return productRepository.findByPriceBetween(priceFrom, priceTo, pageable);
        } else {
            return productRepository.findAll(pageable).getContent();
        }
    }    
}

