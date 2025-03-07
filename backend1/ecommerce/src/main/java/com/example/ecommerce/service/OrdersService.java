package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.OrdersRequest;
import com.example.ecommerce.dto.response.CartResponse;
import com.example.ecommerce.dto.response.OrdersResponse;
import com.example.ecommerce.exception.DataNotFoundException;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductsService productService;

    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private Order_itemsRepository order_itemsRepository;

    @Autowired
    private Order_HistoryService orderHistoryService;

    @Autowired
    private Order_HistoryRepository orderHistoryRepository;

    @Transactional
    public OrdersResponse createOrder(OrdersRequest ordersRequest) {

        Customer customer = customerRepository.findByUsername(ordersRequest.getUsername())
                .orElseThrow(() -> new DataNotFoundException("User not found with username " + ordersRequest.getUsername()));


        List<CartResponse> cartItems = cartService.getCartDetails(customer.getId());

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Keranjang belanja kosong. Tidak dapat membuat pesanan.");
        }


        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQty())
                .sum();

        OrderStatus status = (ordersRequest.getStatus() == null || ordersRequest.getStatus().trim().isEmpty())
                ? OrderStatus.PROCESSING
                : OrderStatus.valueOf(ordersRequest.getStatus().toUpperCase());

        Orders order = new Orders();
        order.setCustomer(customer);
        order.setStatus(status);
        order.setTotalPrice(totalPrice);
        order.setCreatedAt(ordersRequest.getCreatedAt());
        order.setUpdatedAt(ordersRequest.getUpdatedAt());

        Orders savedOrder = ordersRepository.save(order);

        for (CartResponse cartItem : cartItems) {
            Products product = productsRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("Product not found with id " + cartItem.getProductId()));

            if (product.getStock() < cartItem.getQty()) {
                throw new IllegalArgumentException("Stok produk tidak cukup untuk produk " + product.getProduct_name());
            }

            product.setStock(product.getStock() - cartItem.getQty());
            productsRepository.save(product);

            Order_items orderItem = new Order_items();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQty(cartItem.getQty());
            orderItem.setPrice(cartItem.getProductPrice() * cartItem.getQty());

            order_itemsRepository.save(orderItem);
        }

        cartService.clearCart(customer.getId());

        Order_History orderHistory = new Order_History();
        orderHistory.setOrder(savedOrder);
        orderHistory.setStatus(String.valueOf(savedOrder.getStatus()));
        orderHistory.setCreatedAt(savedOrder.getCreatedAt());
        orderHistory.setUpdatedAt(savedOrder.getUpdatedAt());

        orderHistoryRepository.save(orderHistory);

        return convertToResponse(savedOrder);
    }


    @Transactional
    public List<OrdersResponse> getAllOrders() {
        try {
            List<Orders> orders = ordersRepository.findAll();
            return orders.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch orders: " + e.getMessage());
        }
    }

    @Transactional
    public OrdersResponse getOrderById(Long id) {
        Orders order = ordersRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found with id " + id));
        return convertToResponse(order);
    }


    @Transactional
    public void deleteOrder(Long id) {
        if (!ordersRepository.existsById(id)) {
            throw new DataNotFoundException("Order not found with id " + id);
        }
        ordersRepository.deleteById(id);
    }

    @Transactional
    public OrdersResponse updateOrderStatus(Long id, OrdersRequest request) {
        Orders order = ordersRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found with id " + id));

        try {
            OrderStatus newStatus = OrderStatus.valueOf(request.getStatus().toUpperCase());
            order.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + request.getStatus());
        }

        Orders updatedOrder = ordersRepository.save(order);

        Order_History orderHistory = new Order_History();
        orderHistory.setOrder(updatedOrder);
        orderHistory.setStatus(String.valueOf(updatedOrder.getStatus()));
        orderHistory.setCreatedAt(updatedOrder.getCreatedAt());
        orderHistory.setUpdatedAt(updatedOrder.getUpdatedAt());

        orderHistoryRepository.save(orderHistory);

        return convertToResponse(updatedOrder);
    }



    @Transactional
    public List<OrdersResponse> getOrdersByCustomerId(UUID customerId) {
        List<Orders> orders = ordersRepository.findByCustomer_Id(customerId);
        if (orders.isEmpty()) {
            throw new DataNotFoundException("No orders found for customer ID: " + customerId);
        }
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OrdersResponse> getOrdersByUsername(String username) {
        List<Orders> orders = ordersRepository.findByCustomer_Username(username);
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public List<OrdersResponse> getOrdersByStatus(String status) {
        List<Orders> orders = ordersRepository.findByStatus(status);
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    private OrdersResponse convertToResponse(Orders savedOrder) {
        OrdersResponse response = new OrdersResponse();
        response.setId(savedOrder.getId());
        response.setStatus(savedOrder.getStatus());
        response.setTotalPrice(savedOrder.getTotalPrice());
        response.setCreatedAt(savedOrder.getCreatedAt());
        response.setUpdatedAt(savedOrder.getUpdatedAt());

        Customer customer = savedOrder.getCustomer();
        if (customer != null) {
            OrdersResponse.CustomerData customerData = new OrdersResponse.CustomerData();
            customerData.setCustomerId(customer.getId());
            customerData.setRole(customer.getRole());
            response.setCustomer(customerData);

            response.setUsername(customer.getUsername());
        } else {
            response.setCustomer(null);
            response.setUsername(null);
        }

        return response;
    }

}