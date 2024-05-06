package com.projects.order_ms.dtos;

import lombok.Data;

@Data
public class ProductDTO {
    private long id;
    private String title;
    private String description;
    private double price;
    private String image;
    private int availableQuantity;
    private CategoryDTO category;
}
