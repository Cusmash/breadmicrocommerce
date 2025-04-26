package com.bread.productservice.repository;

import com.bread.productservice.model.Product;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByTypeIgnoreCase(String type, Pageable pageable);

    List<Product> findByFlavorIgnoreCase(String flavor, Pageable pageable);

    List<Product> findByOnSale(boolean onSale, Pageable pageable);

    List<Product> findByPriceBetween(Double priceFrom, Double priceTo, Pageable pageable);

    List<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Product> findByTypeIgnoreCaseAndFlavorIgnoreCaseAndPriceBetween(String type, String flavor, Double priceFrom, Double priceTo, Pageable pageable);

    List<Product> findByTypeIgnoreCaseAndPriceBetween(String type, Double priceFrom, Double priceTo, Pageable pageable);

    List<Product> findByFlavorIgnoreCaseAndPriceBetween(String flavor, Double priceFrom, Double priceTo, Pageable pageable);
}
