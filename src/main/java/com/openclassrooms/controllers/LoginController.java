package com.openclassrooms.controllers;

import com.openclassrooms.dto.authentication.LoginRequest;
import com.openclassrooms.dto.authentication.LoginResponse;
import com.openclassrooms.services.JWTService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Log
public class LoginController {
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginController(JWTService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates user credentials and returns JWT token")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        String token = jwtService.generateToken(authentication);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}