package com.example.ecommerce.dto.request;

import lombok.Data;

@Data
public class Order_HistoryRequest {
    private String order_id;
    private String status;
}
