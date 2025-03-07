package com.example.ecommerce.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductsResponse {
    private Long id;
    private CategoryData category_id;
    private String product;
    private String description;
    private double price;
    private int stock;
    private boolean status;
    private String images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductsResponse(){this.category_id = new CategoryData();}

    public void  setCategoryId(Long id){this.category_id.setId(id);}

    public void  setCategoryName(String name){this.category_id.setName(name);}
    @Data
    class CategoryData{
        private Long id;
        private String name;
    }
}
