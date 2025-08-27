package com.example.inventory_management_system.controller;

import com.example.inventory_management_system.entity.Vendor;
import com.example.inventory_management_system.repositories.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VendorController {

    private final VendorRepository vendorRepository;

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors() {
        List<Vendor> vendors = vendorRepository.findAll();
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Long id) {
        return vendorRepository.findById(id)
                .map(vendor -> ResponseEntity.ok(vendor))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<Vendor> createVendor(@Valid @RequestBody Vendor vendor) {
        if (vendorRepository.existsByName(vendor.getName())) {
            return ResponseEntity.badRequest().build();
        }
        Vendor savedVendor = vendorRepository.save(vendor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVendor);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<Vendor> updateVendor(@PathVariable Long id, @Valid @RequestBody Vendor vendorDetails) {
        return vendorRepository.findById(id)
                .map(vendor -> {
                    vendor.setName(vendorDetails.getName());
                    vendor.setContactPerson(vendorDetails.getContactPerson());
                    vendor.setEmail(vendorDetails.getEmail());
                    vendor.setPhone(vendorDetails.getPhone());
                    vendor.setAddress(vendorDetails.getAddress());
                    return ResponseEntity.ok(vendorRepository.save(vendor));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteVendor(@PathVariable Long id) {
        return vendorRepository.findById(id)
                .map(vendor -> {
                    vendorRepository.delete(vendor);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
