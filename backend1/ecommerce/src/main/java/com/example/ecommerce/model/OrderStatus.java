package com.example.ecommerce.model;

public enum OrderStatus {
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELED;

    public String toUpperCaseString() {
        return this.name().toUpperCase();
    }

    public String toUpperCase() {
        return this.name().toUpperCase();
    }

    public CharSequence trim() {
        return this.name().trim();
    }
}
