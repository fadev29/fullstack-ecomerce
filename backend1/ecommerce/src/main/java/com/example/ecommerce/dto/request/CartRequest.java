package com.example.ecommerce.dto.request;

import lombok.Data;

@Data
public class CartRequest {
    private Long id;
    private Long product_id;
    private Long customer_id;
    private int qty;
}
