package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.OrdersRequest;
import com.example.ecommerce.dto.response.OrdersResponse;
import com.example.ecommerce.exception.DataNotFoundException;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.service.OrdersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrdersResponse> createOrder(@RequestBody OrdersRequest ordersRequest) {
        OrdersResponse orderResponse = ordersService.createOrder(ordersRequest);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrdersResponse>> getAllOrders() {
       try{
           List<OrdersResponse> orders = ordersService.getAllOrders();
           return ResponseEntity.status(HttpStatus.OK).body(orders);
       }catch (DataNotFoundException e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                   .body(null);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdersResponse> getOrderById(@PathVariable Long id) {
        try {
            OrdersResponse order = ordersService.getOrderById(id);
            return ResponseEntity.ok(order);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrdersResponse>> getOrdersByCustomerId(@PathVariable UUID customerId) {
        try {
            List<OrdersResponse> orders = ordersService.getOrdersByCustomerId(customerId);
            return ResponseEntity.ok(orders);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrdersResponse>> getOrdersByStatus(@PathVariable String status) {
        try {
            List<OrdersResponse> orders = ordersService.getOrdersByStatus(status);
            return ResponseEntity.ok(orders);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OrdersResponse> updateOrderStatus(@PathVariable Long id, @RequestBody OrdersRequest request) {
        try {
            OrdersResponse order = ordersService.updateOrderStatus(id, request);
            return ResponseEntity.ok(order);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            ordersService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/orders/{username}")
    public ResponseEntity<List<OrdersResponse>> getOrdersByUsername(@PathVariable String username) {
        try {
            List<OrdersResponse> orders = ordersService.getOrdersByUsername(username);
            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(orders);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }


}
