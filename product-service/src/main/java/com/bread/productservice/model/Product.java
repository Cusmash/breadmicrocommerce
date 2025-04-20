package com.bread.productservice.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data // Lombok genera getters, setters, equals, hashcode automaticamente
@Document(collection = "products") // Nombre de la colección en MongoDB - le dice a Spring Data Mongo que esta clase es un documento
public class Product implements Serializable { //Redis por default serializa los objetos a JSON, por lo que es necesario implementar Serializable para que funcione correctamente en Redis
    
    @Id
    private String id;
    
    private String name;
    
    private String description;
    
    private double price;
    
    private int quantity;

    private String imgUrl;
}
