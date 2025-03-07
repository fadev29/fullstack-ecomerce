package com.example.ecommerce.controller;

import com.example.ecommerce.dto.response.CartResponse;
import com.example.ecommerce.service.CartService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestParam String username,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        try {
            // Call the service with username instead of customerId
            cartService.addToCart(username, productId, quantity);
            return ResponseEntity.ok("Product added to cart successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/details")
    public ResponseEntity<List<CartResponse>> getCartDetails(@RequestParam @NotNull UUID customerId) {
        try {
            List<CartResponse> cartDetails = cartService.getCartDetails(customerId);
            return ResponseEntity.status(HttpStatus.OK).body(cartDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @GetMapping("/{username}")
    public ResponseEntity<List<CartResponse>> getCartDetailsByUsername(@PathVariable String username) {
        try {
            List<CartResponse> cartDetails = cartService.getAllCartUsername(username);
            return ResponseEntity.status(HttpStatus.OK).body(cartDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<List<CartResponse>> deleteFromCart(
            @RequestParam @NotNull UUID customerId,
            @RequestParam @NotNull Long productId) {
        try {
            List<CartResponse> updatedCart = cartService.deleteFromCart(customerId, productId);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCart(
            @RequestParam @NotNull UUID customerId,
            @RequestParam @NotNull Long productId,
            @RequestParam int quantity) {
        try {
            cartService.updateCart(customerId, productId, quantity);
            return ResponseEntity.status(HttpStatus.OK).body("Cart updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestParam @NotNull UUID customerId) {
        try {
            cartService.clearCart(customerId);
            return ResponseEntity.status(HttpStatus.OK).body("Cart cleared successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

