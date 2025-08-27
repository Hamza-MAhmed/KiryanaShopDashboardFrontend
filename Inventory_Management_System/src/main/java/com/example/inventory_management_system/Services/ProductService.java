package com.example.inventory_management_system.Services;

import com.example.inventory_management_system.DTOs.ProductDto;
import com.example.inventory_management_system.DTOs.ProductFilter;
import com.example.inventory_management_system.entity.Product;
import com.example.inventory_management_system.entity.Category;
import com.example.inventory_management_system.entity.Vendor;
import com.example.inventory_management_system.repositories.ProductRepository;
import com.example.inventory_management_system.repositories.CategoryRepository;
import com.example.inventory_management_system.repositories.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Page<ProductDto> getAllProducts(ProductFilter filter) {
        Specification<Product> spec = createSpecification(filter);

        Sort sort = Sort.by(Sort.Direction.fromString(filter.getSortDir()), filter.getSortBy());
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

        Page<Product> products = productRepository.findAll(spec, pageable);
        return products.map(this::convertToDto);
    }

    public List<ProductDto> getLowStockProducts() {
        List<Product> products = productRepository.findLowStockProducts();
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return convertToDto(product);
    }

    public ProductDto createProduct(ProductDto productDto) {
        if (productDto.getSku() != null && productRepository.existsBySku(productDto.getSku())) {
            throw new RuntimeException("Product with SKU already exists: " + productDto.getSku());
        }

        Product product = convertToEntity(productDto);
        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (productDto.getSku() != null && !productDto.getSku().equals(existingProduct.getSku())
                && productRepository.existsBySku(productDto.getSku())) {
            throw new RuntimeException("Product with SKU already exists: " + productDto.getSku());
        }

        updateProductFields(existingProduct, productDto);
        Product savedProduct = productRepository.save(existingProduct);
        return convertToDto(savedProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private Specification<Product> createSpecification(ProductFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), filter.getCategoryId()));
            }


            if (filter.getVendorId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("vendor").get("id"), filter.getVendorId()));
            }

            if (filter.getLowStock() != null && filter.getLowStock()) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("quantity"), root.get("lowStockThreshold")));
            }

            if (filter.getSearch() != null && !filter.getSearch().trim().isEmpty()) {
                String searchPattern = "%" + filter.getSearch().toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), searchPattern);
                Predicate skuPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("sku")), searchPattern);
                predicates.add(criteriaBuilder.or(namePredicate, skuPredicate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setSku(product.getSku());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setLowStockThreshold(product.getLowStockThreshold());
        dto.setCategoryId(product.getCategory().getId());
        dto.setCategoryName(product.getCategory().getName());
        dto.setVendorId(product.getVendor().getId());
        dto.setVendorName(product.getVendor().getName());
        dto.setLowStock(product.isLowStock());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }

    private Product convertToEntity(ProductDto dto) {
        Product product = new Product();
        updateProductFields(product, dto);
        return product;
    }

    private void updateProductFields(Product product, ProductDto dto) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setSku(dto.getSku());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setLowStockThreshold(dto.getLowStockThreshold());

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
        product.setCategory(category);

        Vendor vendor = vendorRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + dto.getVendorId()));
        product.setVendor(vendor);
    }
}