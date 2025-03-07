package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.CategoryRequest;
import com.example.ecommerce.dto.response.CategoryResponse;
import com.example.ecommerce.exception.DataNotFoundException;
import com.example.ecommerce.exception.DuplicateDataException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public List<CategoryResponse> findAll() {
        try {
            return categoryRepository.findAll()
                    .stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch categories", e);
        }
    }

    @Transactional
    public CategoryResponse findById(Long id) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            return convertToResponse(category);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch category", e);
        }
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        try{
            if(categoryRepository.findByName(categoryRequest.getName()).isPresent()){
                throw new RuntimeException("Category already exists");
            }
            Category category = new Category();
            category.setName(categoryRequest.getName());
            Category savedCategory = categoryRepository.save(category);
            return convertToResponse(savedCategory);
        }catch (DuplicateDataException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException("Failed to create category", e);
        }
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        try{
            if(categoryRepository.findByName(categoryRequest.getName()).isPresent()){
                throw new RuntimeException("Category already exists");
            }
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            category.setName(categoryRequest.getName());
            Category savedCategory = categoryRepository.save(category);
            return convertToResponse(savedCategory);
        }catch (DuplicateDataException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException("Failed to update category", e);
        }

    }

    @Transactional
    public void deleteCategory(Long id) {
       try{
           if(!categoryRepository.existsById(id)){
               throw new RuntimeException("Category not found");
           }
           categoryRepository.deleteById(id);

       }catch (DataNotFoundException e){
           throw e;
       }catch (Exception e){
           throw new RuntimeException("Failed to delete category", e);
       }
    }

    public Optional<CategoryResponse> findByName(String name){
        try{
            return categoryRepository.findByName(name)
                    .map(this::convertToResponse);
        }catch (Exception e){
            throw new RuntimeException("Failed to find category", e);
        }
        }

    private CategoryResponse convertToResponse(@NotNull Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }

}

