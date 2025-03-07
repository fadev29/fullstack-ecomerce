package com.example.ecommerce.controller;

import com.example.ecommerce.dto.response.Order_itemsResponse;
import com.example.ecommerce.service.Order_itemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order_items")
public class OrderitemsController {

    private final Order_itemsService orderItemsService;

    @Autowired
    public OrderitemsController(Order_itemsService orderItemsService) {
        this.orderItemsService = orderItemsService;
    }

    @GetMapping("/order/{id}")
    public List<Order_itemsResponse> getOrderItemsByOrderId(@PathVariable("id") Long id) {
        List<Order_itemsResponse> orderItems = orderItemsService.findByOrder(id);
        if (orderItems == null || orderItems.isEmpty()) {
            throw new RuntimeException("No order items found for the provided order ID.");
        }
        return orderItems;
    }
}
