package com.openclassrooms.controllers;

import com.openclassrooms.dto.authentication.RegisterRequest;
import com.openclassrooms.exceptions.ApiException;
import com.openclassrooms.models.User;
import com.openclassrooms.services.JWTService;
import com.openclassrooms.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Log
public class RegisterController {
    private final UserService userService;
    private final JWTService jwtService;

    public RegisterController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Creates a new user account and returns JWT token")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            // Register the user
            User user = userService.registerUser(request);

            // Create authentication token
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

            // Generate JWT token
            String token = jwtService.generateToken(authentication);

            // Create response
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (ApiException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, e.getStatus());
        }
    }
}