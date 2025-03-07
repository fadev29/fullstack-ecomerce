package com.example.ecommerce.repository;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCustomerIdAndProductId(UUID customerId, Long productId);

    List<Cart> findByCustomerId(UUID customerId);

    void deleteByCustomerIdAndProductId(UUID customerId, Long productId);

    void deleteByCustomerId(UUID customerId);

    Optional<Cart> findByCustomerAndProduct(Customer customer, Products product);

    List<Cart> findByCustomer(Customer customer);

    void deleteByCustomer(Customer customer);

    void deleteByCustomerAndProduct(Customer customer, Products product);
}
