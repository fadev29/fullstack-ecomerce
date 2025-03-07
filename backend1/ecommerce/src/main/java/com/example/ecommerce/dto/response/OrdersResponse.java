package com.example.ecommerce.dto.response;

import com.example.ecommerce.model.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrdersResponse {
    private Long id;
    private CustomerData customer;
    private String username;
    private OrderStatus status;
    private double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrdersResponse() {
        this.customer = new CustomerData();
    }

    public void setCustomerId(UUID id) {
    }

    public void setRole(String role) {
    }

    @Data
    public static class CustomerData {
        private UUID customerId;
        private String role;
    }
}
