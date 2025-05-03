package com.bread.productservice.controller;

import com.bread.productservice.dto.PagedResponseDTO;
import com.bread.productservice.dto.ProductInputDTO;
import com.bread.productservice.dto.ProductFilterInput;
import com.bread.productservice.model.Product;
import com.bread.productservice.service.ProductService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class ProductGraphQLController {

    private final ProductService productService;

    public ProductGraphQLController(ProductService productService) {
        this.productService = productService;
    }

    @QueryMapping
    public PagedResponseDTO<Product> getAllProducts(
            @Argument Integer page,
            @Argument Integer size,
            @Argument String sort
    ) {
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;
        String sortDirection = (sort != null && sort.equalsIgnoreCase("DESC")) ? "DESC" : "ASC";
        log.info("Fetching all products. Page: {}, Size: {}, Sort: {}", pageNumber, pageSize, sortDirection);
        return productService.getAllProductsPagedSorted(pageNumber, pageSize, sortDirection);
    }

    @QueryMapping
    public PagedResponseDTO<Product> getFilteredProducts(
            @Argument ProductFilterInput filter,
            @Argument Integer page,
            @Argument Integer size,
            @Argument String sort) {
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;
        log.info("Filtering products with filter: {}, Page: {}, Size: {}, Sort: {}", filter, pageNumber, pageSize, sort);
        String sortDirection = (sort != null && sort.equalsIgnoreCase("DESC")) ? "DESC" : "ASC";
        if (filter == null) {
            return productService.getAllProductsPagedSorted(pageNumber, pageSize, sortDirection);
        }
        return productService.getFilteredProducts(filter, pageNumber, pageSize, sortDirection);
    }

    @QueryMapping
    public Optional<Product> getProductById(@Argument String id) {
        log.info("Fetching product by ID: {}", id);
        return productService.getProductById(id);
    }

    @MutationMapping
    public Product createProduct(@Argument @Valid ProductInputDTO input) {
        log.info("Creating product: {}", input.getName());
        Product newProduct = new Product();
        newProduct.setName(input.getName());
        newProduct.setDescription(input.getDescription());
        newProduct.setPrice(input.getPrice());
        newProduct.setQuantity(input.getQuantity());
        newProduct.setImgUrl(input.getImgUrl());
        newProduct.setType(input.getType());
        newProduct.setFlavor(input.getFlavor());
        newProduct.setOnSale(input.isOnSale());
        newProduct.setDiscountPercentage(input.getDiscountPercentage());
        return productService.createProduct(newProduct);
    }

    @MutationMapping
    public Product updateProduct(@Argument String id, @Argument @Valid ProductInputDTO input) {
        log.info("Updating product with ID: {}", id);
        Product updatedProduct = new Product();
        updatedProduct.setName(input.getName());
        updatedProduct.setDescription(input.getDescription());
        updatedProduct.setPrice(input.getPrice());
        updatedProduct.setQuantity(input.getQuantity());
        updatedProduct.setImgUrl(input.getImgUrl());
        updatedProduct.setType(input.getType());
        updatedProduct.setFlavor(input.getFlavor());
        updatedProduct.setOnSale(input.isOnSale());
        updatedProduct.setDiscountPercentage(input.getDiscountPercentage());
        return productService.updateProduct(id, updatedProduct);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument String id) {
        log.warn("Deleting product with ID: {}", id);
        productService.deleteProduct(id);
        return true;
    }

    @QueryMapping
    public List<Product> searchProductsByName(
            @Argument String name,
            @Argument Integer page,
            @Argument Integer size) {
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;
        log.info("Searching products by name: '{}', page: {}, size: {}", name, page, size);
        return productService.searchProductsByName(name, pageNumber, pageSize);
    }

    @QueryMapping
    public List<Product> filterProducts(
            @Argument String type,
            @Argument Float priceFrom,
            @Argument Float priceTo,
            @Argument Integer page,
            @Argument Integer size) {
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;
        log.info("Filtering products by type: {}, priceFrom: {}, priceTo: {}", type, priceFrom, priceTo);
        return productService.filterProducts(type, priceFrom != null ? priceFrom.doubleValue() : null,
                priceTo != null ? priceTo.doubleValue() : null,
                pageNumber, pageSize);
    }

    @QueryMapping
    public List<String> getAvailableFlavors() {
        return productService.getAvailableFlavors();
    }

    @QueryMapping
    public List<String> getAvailableTypes() {
        return productService.getAvailableTypes();
    }

}
