package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.ProductsRequest;
import com.example.ecommerce.dto.response.ApiResponse;
import com.example.ecommerce.dto.response.ProductsResponse;
import com.example.ecommerce.exception.DataNotFoundException;
import com.example.ecommerce.exception.DuplicateDataException;
import com.example.ecommerce.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createProducts(@ModelAttribute ProductsRequest request) {
        try {
            ProductsResponse response = productsService.createProduct(request);
            return ResponseEntity.ok(new ApiResponse<>(200, response));
        } catch (DuplicateDataException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(409, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, e.getMessage()));
        }
    }


    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        try{
            Page<ProductsResponse> productsResponses = productsService.findAll(page, size);
            return ResponseEntity.ok(new ApiResponse<>(200, productsResponses));
        }catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "failed to retrive category" + e.getMessage()));

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            ProductsResponse response = productsService.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Product not found with id " + id));
            return ResponseEntity.ok(new ApiResponse<>(200, response));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, e.getMessage()));
        }
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateProducts(@PathVariable Long id, @ModelAttribute ProductsRequest request) {
        if (request.getCategory_id() == null || request.getCategory_id() <= 0) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Invalid category ID"));
        }

        try {
            ProductsResponse response = productsService.updateProduct(id, request);
            return ResponseEntity.ok(new ApiResponse<>(200, response));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage()));
        } catch (DuplicateDataException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(409, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, e.getMessage()));
        }
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productsService.deletedProduct(id);
            return ResponseEntity.ok(new ApiResponse<>(200, "Product deleted successfully"));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductsResponse>>> serchProducts(@RequestParam String keyword) {
        List<ProductsResponse> productsResponses = productsService.serchProducts(keyword);
        return ResponseEntity.ok(new ApiResponse<>(200, productsResponses));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<ProductsResponse>>> searchProductsByCategory(@RequestParam Long categoryId) {
        List<ProductsResponse> productsResponses = productsService.filterProductsByCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse<>(200, productsResponses));
    }

    @GetMapping("/imagesPath/{imagesPath}")
    public ResponseEntity<byte[]> getProductsImage(@PathVariable("imagesPath") String imagesPath) throws IOException {
        Path path = Paths.get(uploadDir, imagesPath);

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Files.readAllBytes(path);
        String fileExtension = imagesPath.substring(imagesPath.lastIndexOf(".") + 1);

        MediaType mediaType = switch (fileExtension.toLowerCase()) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };

        return ResponseEntity.ok().contentType(mediaType).body(imageBytes);
    }

}
