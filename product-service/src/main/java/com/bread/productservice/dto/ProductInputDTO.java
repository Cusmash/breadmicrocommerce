package com.bread.productservice.dto;

import com.bread.productservice.model.Flavor;
import com.bread.productservice.model.ProductType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductInputDTO {

    @NotBlank(message = "El nombre no puede ser vacío")
    private String name;

    @NotBlank(message = "La descripción no puede ser vacía")
    private String description;

    @NotBlank(message = "La URL de la imagen no puede ser vacía")
    private String imgUrl;

    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double price;

    @NotNull(message = "La cantidad no puede ser nula")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer quantity;

    @NotNull(message = "El tipo de producto es requerido")
    private ProductType type;

    private boolean onSale = false;

    @NotNull(message = "El tipo de producto es requerido")
    private Flavor flavor;

    @PositiveOrZero(message = "El descuento debe ser 0 o mayor")
    private Double discountPercentage;
}
