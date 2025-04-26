package com.bread.productservice.dto;

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

    @NotBlank(message = "Type cannot be empty")
    private String type;

    private boolean onSale = false;
    
    @NotBlank(message = "flavor cannot be empty")
    private String flavor;

    @PositiveOrZero(message = "Discount percentage must be positive or zero")
    private Double discountPercentage;
}
