package com.example.inventory_management_system.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;
    private String sku;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be non-negative")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be non-negative")
    private Integer quantity;

    private Integer lowStockThreshold = 10;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
    private String categoryName;

    @NotNull(message = "Vendor ID is required")
    private Long vendorId;
    private String vendorName;

    private boolean lowStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}