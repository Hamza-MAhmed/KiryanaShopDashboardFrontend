package com.example.inventory_management_system.DTOs;

import lombok.Data;
import lombok.AllArgsConstructor;
import com.example.inventory_management_system.entity.Role;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private Role role;
    private String message;
}