package com.bread.productservice.service;

import com.bread.productservice.dto.PagedResponseDTO;
import com.bread.productservice.dto.ProductFilterInput;
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
    
    public PagedResponseDTO<Product> getFilteredProducts(ProductFilterInput filter, int page, int size, String sort) {
        Sort.Direction direction = sort.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "price"));

        List<Product> filteredProducts = productRepository.findAll().stream()
                .filter(p -> filter.getType() == null || p.getType() == filter.getType())
                .filter(p -> filter.getFlavor() == null || p.getFlavor() == filter.getFlavor())
                .filter(p -> filter.getOnSale() == null || p.isOnSale() == filter.getOnSale())
                .filter(p -> filter.getPriceFrom() == null || p.getPrice() >= filter.getPriceFrom())
                .filter(p -> filter.getPriceTo() == null || p.getPrice() <= filter.getPriceTo())
                .sorted((p1, p2) -> {
                    if (direction == Sort.Direction.ASC) {
                        return Double.compare(p1.getPrice(), p2.getPrice());
                    } else {
                        return Double.compare(p2.getPrice(), p1.getPrice());
                    }
                })
                .toList();

        int start = page * size;
        int end = Math.min(start + size, filteredProducts.size());
        List<Product> pagedList = filteredProducts.subList(Math.min(start, filteredProducts.size()), end);

        return new PagedResponseDTO<>(
                pagedList,
                page,
                size,
                filteredProducts.size(),
                (int) Math.ceil((double) filteredProducts.size() / size),
                end >= filteredProducts.size());
    }
}

