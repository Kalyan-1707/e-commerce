package com.ecommerce.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    @Size(min = 3, message = "Product name must contains atleast 3 characters")
    private String productName;
    
    @NotBlank
    @Size(min = 3, message = "Product name must contains atleast 3 characters")
    private String description;
    private Double discount;
    private String image;
    private Double price;
    private Integer quantity;
    private Double specialPrice;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
