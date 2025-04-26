package com.bread.productservice.dto;

import com.bread.productservice.model.ProductType;

import java.util.List;

import com.bread.productservice.model.Flavor;

import lombok.Data;

@Data
public class ProductFilterInput {
    private List<ProductType> types; 
    private List<Flavor> flavors;    
    private Boolean onSale;
    private Double priceFrom;
    private Double priceTo;
}
