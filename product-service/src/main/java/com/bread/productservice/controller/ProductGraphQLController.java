package com.bread.productservice.controller;

import com.bread.productservice.dto.ProductInputDTO;
import com.bread.productservice.model.Product;
import com.bread.productservice.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductGraphQLController {

    private final ProductService productService;

    public ProductGraphQLController(ProductService productService) {
        this.productService = productService;
    }

    @QueryMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @QueryMapping
    public Optional<Product> getProductById(@Argument String id) {
        return productService.getProductById(id);
    }

    @MutationMapping
    public Product createProduct(@Argument @Valid ProductInputDTO input) {
        Product newProduct = new Product();
        newProduct.setName(input.getName());
        newProduct.setDescription(input.getDescription());
        newProduct.setPrice(input.getPrice());
        newProduct.setQuantity(input.getQuantity());
        newProduct.setImgUrl(input.getImgUrl());
        return productService.createProduct(newProduct);
    }


    @MutationMapping
    public Product updateProduct(
            @Argument String id,
            @Argument String name,
            @Argument String description,
            @Argument Float price,
            @Argument Integer quantity,
            @Argument String imgUrl) {

        Product updatedProduct = new Product();
        updatedProduct.setName(name);
        updatedProduct.setDescription(description);
        updatedProduct.setPrice(price);
        updatedProduct.setQuantity(quantity);
        updatedProduct.setImgUrl(imgUrl);

        return productService.updateProduct(id, updatedProduct);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument String id) {
        productService.deleteProduct(id);
        return true;
    }
}
