package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
@Entity
public class Order_items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Products product;

    @Column(name = "qty",nullable = false)
    private int qty;

    @Column(name = "total_price",nullable = false)
    private double totalPrice;

    public void setCreatedAt(LocalDateTime now) {
    }

    public void setUpdatedAt(LocalDateTime now) {

    }

    public int getQuantity() {
        return qty;
    }

    public double getPrice() {
        return product.getPrice();
    }


    public void setPrice(double v) {

    }
}
