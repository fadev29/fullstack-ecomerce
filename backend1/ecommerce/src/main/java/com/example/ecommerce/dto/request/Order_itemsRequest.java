package com.example.ecommerce.dto.request;

import lombok.Data;

@Data
public class Order_itemsRequest {
    private Long product_id;
    private Long order_id;
    private int quantity;
    private double totalPrice;
}
