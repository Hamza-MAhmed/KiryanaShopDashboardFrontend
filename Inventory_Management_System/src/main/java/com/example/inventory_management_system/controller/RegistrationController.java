package com.example.inventory_management_system.controller;

import com.example.inventory_management_system.DTOs.RegistrationRequest;
import com.example.inventory_management_system.Services.RegistrationService;
import com.example.inventory_management_system.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest request) {
        User user = registrationService.registerUser(request);
        return ResponseEntity.ok("User registered: " + user.getUsername());
    }
}