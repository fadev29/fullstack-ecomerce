package com.example.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponse {
    private Long id;
    private UUID customerId;
    private Long productId;
    private String productName;
    private int qty;
    private double price;
    private double totalPrice;

    public CartResponse(Long id, UUID customerId, Long productId, String productName, int qty, double price, double totalPrice) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.productName = productName;
        this.qty = qty;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public double getProductPrice() {
        return price;
    }


}
