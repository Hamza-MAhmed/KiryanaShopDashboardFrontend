package com.example.inventory_management_system.DTOs;



import lombok.Data;

@Data
public class ProductFilter {
    private Long categoryId;
    private Long vendorId;
    private Boolean lowStock;
    private String search;
    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "name";
    private String sortDir = "asc";
}