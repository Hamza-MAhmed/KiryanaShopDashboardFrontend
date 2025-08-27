package com.example.inventory_management_system.Services;

import com.example.inventory_management_system.DTOs.AuthRequest;
import com.example.inventory_management_system.DTOs.AuthResponse;
import com.example.inventory_management_system.entity.User;
import com.example.inventory_management_system.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthResponse authenticate(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            log.info("Authenticating user: {}", request.getUsername());

            User user = (User) authentication.getPrincipal();
            log.info("Authenticated user object: {}", user);
            String token = jwtService.generateToken(user);

            return new AuthResponse(token, user.getUsername(), user.getRole(), "Authentication successful");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }
}