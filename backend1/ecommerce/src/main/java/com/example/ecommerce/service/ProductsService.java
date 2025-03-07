package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.ProductsRequest;
import com.example.ecommerce.dto.response.ProductsResponse;
import com.example.ecommerce.exception.DataNotFoundException;
import com.example.ecommerce.exception.DuplicateDataException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Products;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductsService {
    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final String IMAGE_DIRECTORY = "src/main/resources/static/images/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_FILE_TYPES = {"image/jpeg", "image/png", "image/jpg"};

    @Transactional
    public ProductsResponse createProduct(ProductsRequest productsRequest) {
        try {
            Products products = new Products();
            if (productsRequest.getProduct() == null || productsRequest.getProduct().trim().isEmpty()) {
                throw new IllegalArgumentException("Product name cannot be null or empty");
            }
            Category category = categoryRepository.findById(productsRequest.getCategory_id())
                    .orElseThrow(() -> new DataNotFoundException("Category not found"));
            products.setCategory(category);
            products.setName(productsRequest.getProduct());
            products.setDescription(productsRequest.getDescription());
            products.setPrice(productsRequest.getPrice());
            products.setStock(productsRequest.getStock());
            products.setStatus(productsRequest.getStatus());

            if (productsRequest.getImages() != null && !productsRequest.getImages().isEmpty()) {
                products.setImages(saveImage(productsRequest.getImages()));
            }


            productsRepository.save(products);
            return convertToResponse(products);
        } catch (DuplicateDataException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create product: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Optional<ProductsResponse> findById(Long id) {
        return productsRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional
    public Page<ProductsResponse> findAllAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Products> productsPage = productsRepository.findAll(pageable);
        return productsPage.map(this::convertToResponse);
    }

    @Transactional
    public Page<ProductsResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Products> productsPage = productsRepository.findAll(pageable);
        return productsPage.map(this::convertToResponse);
    }

    @Transactional
    public ProductsResponse updateProduct(Long id, ProductsRequest productsRequest) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }

            System.out.println("Updating product with ID: " + id);

            Products products = productsRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Product not found with id " + id));

            // Update hanya jika field tidak null
            if (productsRequest.getCategory_id() != null) {
                Category category = categoryRepository.findById(productsRequest.getCategory_id())
                        .orElseThrow(() -> new DataNotFoundException("Category not found"));
                products.setCategory(category);
            }
            if (productsRequest.getProduct() != null) {
                products.setName(productsRequest.getProduct());
            }
            if (productsRequest.getDescription() != null) {
                products.setDescription(productsRequest.getDescription());
            }
            if (productsRequest.getPrice() != null) {
                products.setPrice(productsRequest.getPrice());
            }
            if (productsRequest.getStock() != null) {
                products.setStock(productsRequest.getStock());
            }
            if (productsRequest.getStatus()) {
                products.setStatus(productsRequest.getStatus());
            }
            if (productsRequest.getImages() != null && !productsRequest.getImages().isEmpty()) {
                products.setImages(saveImage(productsRequest.getImages()));
            }

            // Simpan perubahan
            Products updatedProduct = productsRepository.save(products);
            return convertToResponse(updatedProduct);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update product: " + e.getMessage(), e);
        }
    }



    @Transactional
    public  void  deletedProduct(Long id){
        try{
            if(!productsRepository.existsById(id)){
                throw new DataNotFoundException("Products with Id" + id + "not found");
            }
            productsRepository.deleteById(id);
        }catch (DataNotFoundException e){
            throw e;
        }catch (Exception e){
            throw  new RuntimeException("fai;ed to delete products"+ e.getMessage());
        }
    }


    @Transactional
    public ProductsResponse updateProductStatus(Long id, boolean status) {
        try {
            Products product = productsRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Product not found with id " + id));

            product.setStatus(status);
            Products updatedProduct = productsRepository.save(product);

            return convertToResponse(updatedProduct);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update product status: " + e.getMessage(), e);
        }
    }


    public List<ProductsResponse> serchProducts(String keyword) {
        try{
            return productsRepository.findByNameContainingIgnoreCase(keyword).stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to search products: " + e.getMessage(), e);
        }
    }

    public List<ProductsResponse> filterProductsByCategory(Long categoryId) {
        try {
            return productsRepository.findByCategoryId(categoryId).stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to filter products by category: " + e.getMessage(), e);
        }
    }





    private String saveImage(MultipartFile image) throws IOException {
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Image size exceeds the maximum allowed size of " + MAX_FILE_SIZE / (1024 * 1024) + "MB");
        }

        String fileType = image.getContentType();
        boolean isValidType = false;

        for (String allowedType : ALLOWED_FILE_TYPES) {
            if (allowedType.equals(fileType)) {
                isValidType = true;
                break;
            }
        }

        if (!isValidType) {
            throw new IllegalArgumentException("Invalid file type. Only JPEG, PNG, and JPG files are allowed.");
        }

        String originalFilename = image.getOriginalFilename();
        String customFileName = System.currentTimeMillis() + "_" + originalFilename;

        Path path = Path.of(IMAGE_DIRECTORY + customFileName);
        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return customFileName;
    }

    @Transactional
    public void updateProduct(Products product) {
        productsRepository.save(product);
    }

    public Products getProductById(Long productId) {
        return productsRepository.findById(productId).orElse(null);
    }

    public ProductsResponse convertToResponse(Products products) {
        ProductsResponse response = new ProductsResponse();
        response.setId(products.getId());

        Category category = products.getCategory();
        response.setCategoryId(category.getId());
        response.setCategoryName(category.getName());

        response.setProduct(products.getName());
        response.setDescription(products.getDescription());
        response.setPrice(products.getPrice());
        response.setStock(products.getStock());
        response.setStatus(products.isStatus());
        response.setImages(products.getImages());
        response.setCreatedAt(products.getCreatedAt());
        response.setUpdatedAt(products.getUpdatedAt());

        return response;
    }



}
