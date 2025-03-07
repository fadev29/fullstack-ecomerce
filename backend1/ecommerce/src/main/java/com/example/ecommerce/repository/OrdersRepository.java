package com.example.ecommerce.repository;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrdersRepository extends JpaRepository<Orders ,Long> {
    Optional<Orders> findById(Long id);

    List<Orders> findByStatus(String status);

    Optional<Orders> findByTotalPrice(double totalPrice);
    List<Orders> findByCustomer_Id(UUID customerId);

    List<Orders> findByCustomer_Username(String username);
}
