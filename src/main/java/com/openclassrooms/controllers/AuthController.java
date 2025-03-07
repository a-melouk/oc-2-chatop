package com.openclassrooms.controllers;

import com.openclassrooms.dto.authentication.UserDetails;
import com.openclassrooms.models.User;
import com.openclassrooms.services.JWTService;
import com.openclassrooms.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@Log
public class AuthController {
    private final UserService userService;
    private final JWTService jwtService;

    public AuthController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user details",
            description = "Retrieves details of the currently authenticated user",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDetails> getCurrentUser(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(UserDetails.fromUser(user));
    }
}