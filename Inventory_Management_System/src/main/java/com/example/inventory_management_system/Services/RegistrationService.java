package com.example.inventory_management_system.Services;

import com.example.inventory_management_system.DTOs.RegistrationRequest;
import com.example.inventory_management_system.entity.User;
import com.example.inventory_management_system.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.inventory_management_system.entity.Role;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegistrationRequest request) {
        // Validate uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create and save user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Auto-encode
                .role(Role.ADMIN) // Default role
                .enabled(true)   // Auto-enable
                .build();

        return userRepository.save(user);
    }
}