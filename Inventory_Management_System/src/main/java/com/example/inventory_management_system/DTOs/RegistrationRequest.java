package com.example.inventory_management_system.DTOs;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class RegistrationRequest {
    @NotBlank(message = "Username is required")
    private String username;
    @Email(message = "Email is required")
    private String email;
    @Size(min = 6)
    private String password;
}