package com.example.ecommerce.repository;

import com.example.ecommerce.model.Order_History;
import com.example.ecommerce.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface Order_HistoryRepository extends JpaRepository<Order_History, Long> {
    List<Order_History> findByOrderId(Orders orderId);

    Optional<Order_History> findByOrderIdAndStatus(Orders orderId, String status);

    Optional<Order_History> findByOrderIdAndStatusAndCreatedAt(Orders orderId, String status, LocalDateTime createdAt);

    List<Order_History> findByOrder(Orders order);
}
