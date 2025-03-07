package com.example.ecommerce.dto.request;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.model.Orders;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrdersRequest {
    private Customer customer_id;
    private String username;
    private int quantity;
    private double price;
    private OrderStatus   status;
    private double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Orders getOrder() {
        Orders order = new Orders();
        order.setCustomer(this.customer_id);
        order.setUsername(this.username);
        order.setQuantity(this.quantity);
        order.setPrice(this.price);
        order.setStatus(this.status);
        order.setTotalPrice(this.totalPrice);
        order.setCreatedAt(this.createdAt);
        order.setUpdatedAt(this.updatedAt);
        return order;
    }

    public List<Order_itemsRequest> getItemsRequest() {
        return null;
    }
}
