package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class ProductsRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name cannot exceed 255 characters")
    private String product;

    private String description;
    private Double price;
    private int stock;
    private boolean status;
    private Long category_id;
    private MultipartFile images;


    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public boolean getStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public Long getCategory_id() { return category_id; }
    public void setCategory_id(Long category_id) { this.category_id = category_id; }

    public MultipartFile getImages() { return images; }
    public void setImages(MultipartFile images) { this.images = images; }
}
