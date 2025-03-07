package com.example.ecommerce.dto.response;

import com.example.ecommerce.model.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order_HistoryResponse {
    private Long id;
    private Long order_id;
    private OrderStatus status;
    private LocalDateTime createdAt;




    public Order_HistoryResponse() {
        this.order_id = new DataOrder().getId();

    }

    public void setOrderStatus(OrderStatus status) {
    }

    public void setOrderCreatedAt(LocalDateTime createdAt) {
    }

    @Data
    public static class DataOrder {
        private Long id;
    }
}
