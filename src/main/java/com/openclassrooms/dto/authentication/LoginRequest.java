package com.openclassrooms.dto.authentication;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}