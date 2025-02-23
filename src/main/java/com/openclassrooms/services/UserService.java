package com.openclassrooms.services;

import java.time.LocalDateTime;
import com.openclassrooms.exceptions.EmailAlreadyExistsException;
import com.openclassrooms.exceptions.ResourceNotFoundException;
import com.openclassrooms.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.dto.authentication.RegisterRequest;
import com.openclassrooms.models.User;
import com.openclassrooms.repositories.UserRepository;

import lombok.Data;

@Data
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User registerUser(RegisterRequest registerRequest) {
        // Check if email already exists
        if (emailExists(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException(registerRequest.getEmail());
        }

        // Create new user
        User user = new User();
        try {
            user.setName(registerRequest.getName());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

            // Set timestamps
            String now = LocalDateTime.now().toString();
            user.setCreated_at(now);
            user.setUpdated_at(now);

            // Save and return user
            return userRepository.save(user);
        } catch (Exception e) {
            throw new ValidationException("Error creating user: " + e.getMessage());
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}