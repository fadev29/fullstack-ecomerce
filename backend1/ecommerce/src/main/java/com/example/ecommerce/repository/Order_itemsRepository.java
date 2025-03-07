package com.example.ecommerce.repository;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Order_items;
import com.example.ecommerce.model.Orders;
import com.example.ecommerce.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Order_itemsRepository extends JpaRepository<Order_items, Long> {
    Optional<Order_items> findByOrderIdAndProductId(Orders orderId, Products productId);


    List<Order_items> findByOrder(Orders order);

    Optional<Order_items> findByProductId(Products productId);


    List<Order_items> findByOrderCustomerAndOrderStatus(Customer customer, String status);



}
