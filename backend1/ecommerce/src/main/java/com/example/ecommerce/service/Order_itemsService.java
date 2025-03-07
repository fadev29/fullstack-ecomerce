package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.Order_itemsRequest;
import com.example.ecommerce.dto.response.Order_itemsResponse;
import com.example.ecommerce.model.Order_items;
import com.example.ecommerce.model.Orders;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Products;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.Order_itemsRepository;
import com.example.ecommerce.repository.OrdersRepository;
import com.example.ecommerce.repository.ProductsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Order_itemsService {

    @Autowired
    private Order_itemsRepository orderItemsRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CustomerRepository customerRepository;
    @Transactional
    public  List<Order_itemsResponse> findByOrder(Long id){

        Orders order = ordersRepository.findById(id).orElse(null);
        if (order == null) {
            return null; // Mengembalikan null jika order tidak ditemukan
        }
        List<Order_items> orderItems = orderItemsRepository.findByOrder(order);
        return orderItems.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }




    private Order_itemsResponse convertToResponse(Order_items orderItems) {
        if (orderItems == null) {
            return null; // Mencegah error jika orderItems null
        }

        Order_itemsResponse response = new Order_itemsResponse();
        response.setId(orderItems.getId());

        Orders order = orderItems.getOrder();
        if (order != null) {
            response.setOrderId(new Order_itemsResponse.DataOrder(order.getId()));
            response.setOrderStatus(order.getStatus());
            response.setOrderCreatedAt(order.getCreatedAt());
        }


        Products product = orderItems.getProduct();
        if (product != null) {
            response.setProductId(new Order_itemsResponse.DataProduct(
                    product.getId(),
                    product.getProduct_name(),
                    product.getPrice(),
                    product.getImages()
            ));
        }

        response.setQty(orderItems.getQuantity());
        response.setTotalPrice(orderItems.getPrice());

        return response;
    }
}
