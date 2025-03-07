package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.CategoryRequest;
import com.example.ecommerce.dto.response.ApiResponse;
import com.example.ecommerce.dto.response.CategoryResponse;
import com.example.ecommerce.exception.DataNotFoundException;
import com.example.ecommerce.exception.DuplicateDataException;
import com.example.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<CategoryResponse> responses = categoryService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body( new ApiResponse<>(HttpStatus.OK.value(), responses));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));

        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest){
        try {
            CategoryResponse response = categoryService.createCategory(categoryRequest);
            return ResponseEntity.status(HttpStatus.OK).body( new ApiResponse<>(HttpStatus.OK.value(), response));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryRequest categoryRequest ,@PathVariable Long id){
        try{
            CategoryResponse response = categoryService.updateCategory(id, categoryRequest);
            return ResponseEntity.status(HttpStatus.OK).body( new ApiResponse<>(HttpStatus.OK.value(), response));
        }catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (DuplicateDataException e){
            return ResponseEntity.status(HttpStatus.CONFLICT.value())
                    .body(new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        try{
            categoryService.deleteCategory(id);
            return ResponseEntity.status(HttpStatus.OK).body( new ApiResponse<>(HttpStatus.OK.value(), "Category deleted successfully"));
        }catch (DataNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }


    @GetMapping("/name")
    public ResponseEntity<?> findByName(@RequestParam String name){
        Optional<CategoryResponse> response = categoryService.findByName(name);
        return ResponseEntity.status(HttpStatus.OK).body( new ApiResponse<>(HttpStatus.OK.value(), response));
    }

}
