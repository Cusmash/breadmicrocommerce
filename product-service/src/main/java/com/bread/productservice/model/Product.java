package com.bread.productservice.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok genera getters, setters, equals, hashcode automaticamente
@Document(collection = "products") // Nombre de la colección en MongoDB - le dice a Spring Data Mongo que esta clase es un documento
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable { //Redis por default serializa los objetos a JSON, por lo que es necesario implementar Serializable para que funcione correctamente en Redis
    
    @Id
    private String id;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "The name must not have more than 100 characters")
    private String name;

    @NotBlank(message = "Decription cannot be empty")
    @Size(max = 500, message = "Decription must not have more than 500 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Quantity is required")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer quantity;

    @NotBlank(message = "imgUrl cannot be empty")
    private String imgUrl;

    @NotBlank(message = "Type cannot be empty")
    private ProductType type;
}
