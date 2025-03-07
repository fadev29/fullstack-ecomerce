package com.example.ecommerce.controller;

import com.example.ecommerce.dto.response.Order_HistoryResponse;
import com.example.ecommerce.dto.response.Order_itemsResponse;
import com.example.ecommerce.service.Order_HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/order_history")
public class OrderHistoryController {


    @Autowired
    private Order_HistoryService orderHistoryService;


    @GetMapping("/{id}")
    public List<Order_HistoryResponse> getOderHistoryByordrerId(@PathVariable("id") Long id) {
        List<Order_HistoryResponse> orderHistory = orderHistoryService.findByOrderr(id);
        if (orderHistory == null || orderHistory.isEmpty()) {
            throw new RuntimeException("No order history found for the provided order ID.");
        }
        return orderHistory;
    }
}