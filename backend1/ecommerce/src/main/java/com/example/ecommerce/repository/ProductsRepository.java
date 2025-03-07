package com.example.ecommerce.repository;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    Optional<Products> findById(Long id);

    Optional<Products> findByName(String name);

    List<Products> findByCategoryId(Long categoryId);

    List<Products> findByNameContainingIgnoreCase(String keyword);

}
