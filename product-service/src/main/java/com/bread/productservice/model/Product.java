package com.bread.productservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data // Lombok genera getters, setters, equals, hashcode automaticamente
@Document(collection = "products") // Nombre de la colección en MongoDB - le dice a Spring Data Mongo que esta clase es un documento
public class Product {
    
    @Id
    private String id;
    
    private String name;
    
    private String description;
    
    private double price;
    
    private int quantity;
}
