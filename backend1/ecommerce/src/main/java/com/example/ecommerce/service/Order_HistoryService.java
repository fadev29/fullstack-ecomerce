package com.example.ecommerce.service;

import com.example.ecommerce.dto.response.Order_HistoryResponse;
import com.example.ecommerce.dto.response.Order_itemsResponse;
import com.example.ecommerce.model.Order_History;
import com.example.ecommerce.model.Orders;
import com.example.ecommerce.repository.Order_HistoryRepository;
import com.example.ecommerce.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Order_HistoryService {

    @Autowired
    private Order_HistoryRepository orderHistoryRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    public List<Order_HistoryResponse> findByOrderr(Long id) {
        Orders orders = ordersRepository.findById(id).orElse(null);
        if (orders == null) {
            return null;
        }
        List<Order_History> orderHistories = orderHistoryRepository.findByOrder(orders);
        return orderHistories.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private Order_HistoryResponse convertToResponse(Order_History orderHistory) {
        Order_HistoryResponse orderHistoryResponse = new Order_HistoryResponse();
         Orders orders = orderHistory.getOrder();
        orderHistoryResponse.setId(orders.getId());
        orderHistoryResponse.setStatus(orderHistory.getStatus());
        orderHistoryResponse.setCreatedAt(orderHistory.getCreatedAt());
        return orderHistoryResponse;
    }

}