package com.bread.productservice.repository;

import com.bread.productservice.model.Flavor;
import com.bread.productservice.model.Product;
import com.bread.productservice.model.ProductType;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByType(ProductType type, Pageable pageable);

    List<Product> findByPriceBetween(Double priceFrom, Double priceTo, Pageable pageable);
    
    List<Product> findByTypeAndPriceBetween(ProductType type, Double priceFrom, Double priceTo, Pageable pageable);
    
    List<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Product> findByTypeIn(List<ProductType> types, Pageable pageable);

    List<Product> findByFlavorIn(List<Flavor> flavors, Pageable pageable);

    List<Product> findByTypeInAndFlavorIn(List<ProductType> types, List<Flavor> flavors, Pageable pageable);
}
