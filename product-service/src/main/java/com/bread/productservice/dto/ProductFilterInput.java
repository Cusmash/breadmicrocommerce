package com.bread.productservice.dto;

import com.bread.productservice.model.ProductType;
import com.bread.productservice.model.Flavor;

import lombok.Data;

@Data
public class ProductFilterInput {
    private ProductType type;
    private Flavor flavor;
    private Boolean onSale;
    private Float priceFrom;
    private Float priceTo;
}
