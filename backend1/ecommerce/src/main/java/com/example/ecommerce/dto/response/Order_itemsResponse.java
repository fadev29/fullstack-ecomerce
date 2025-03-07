package com.example.ecommerce.dto.response;

import com.example.ecommerce.model.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order_itemsResponse {
    private Long id;
    private DataOrder orderId;
    private DataProduct productId;
    private int qty;
    private double totalPrice;

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderId(DataOrder orderId) {
        this.orderId = orderId;
    }

    public void setProductId(DataProduct productId) {
        this.productId = productId;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderStatus(OrderStatus status) {
    }

    public void setOrderCreatedAt(LocalDateTime createdAt) {

    }


    public static class DataOrder {
        private Long id;

        public DataOrder(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    public static class DataProduct {
        private Long id;
        private String productName;
        private double price;
        private String images;

        public DataProduct(Long id, String productName, double price, String images) {
            this.id = id;
            this.productName = productName;
            this.price = price;
            this.images = images;
        }


        public Long getId() {
            return id;
        }

        public String getProductName() {
            return productName;
        }

        public double getPrice() {
            return price;
        }

        public String getImages() {
            return images;
        }
    }
}

