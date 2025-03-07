package com.example.ecommerce.service;

import com.example.ecommerce.dto.response.CartResponse;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Products;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductsRepository;
import com.example.ecommerce.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Transactional
    public void addToCart(String username, Long productId, int quantity) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock for product: " + product.getName());
        }
        Cart cart = cartRepository.findByCustomerAndProduct(customer, product)
                .orElse(new Cart(customer, product, 0));
        cart.setQty(cart.getQty() + quantity);
        cartRepository.save(cart);
    }


    @Transactional
   public List<CartResponse> getAllCartUsername(String username) {
       Customer customer = customerRepository.findByUsername(username)
               .orElseThrow(() -> new RuntimeException("Customer not found"));
       return cartRepository.findByCustomer(customer)
               .stream()
               .map(this::convertToResponse)
               .collect(Collectors.toList());

   }
    @Transactional
    public void clearCart(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        cartRepository.deleteByCustomer(customer);
    }

    @Transactional
    public List<CartResponse> deleteFromCart(UUID customerId, Long productId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartRepository.deleteByCustomerAndProduct(customer, product);

        return getCartDetails(customerId);
    }

    public List<CartResponse> getCartDetails(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return cartRepository.findByCustomer(customer)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateCart(UUID customerId, Long productId, int quantity) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByCustomerAndProduct(customer, product)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.setQty(quantity);
        cartRepository.save(cart);
    }




    private CartResponse convertToResponse(Cart cart) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setProductId(cart.getProduct().getId());
        cartResponse.setProductName(cart.getProduct().getName());
        cartResponse.setQty(cart.getQty());
        cartResponse.setPrice(cart.getProduct().getPrice());
        return cartResponse;
    }




}
