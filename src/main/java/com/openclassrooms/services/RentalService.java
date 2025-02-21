package com.openclassrooms.services;

import com.openclassrooms.dto.rentals.RentalDTO;
import com.openclassrooms.exceptions.ResourceNotFoundException;
import com.openclassrooms.models.Rental;
import com.openclassrooms.repositories.RentalRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserService userService;

    public RentalService(RentalRepository rentalRepository, UserService userService) {
        this.rentalRepository = rentalRepository;
        this.userService = userService;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAllByOrderByCreatedAtDesc();
    }

    public Rental getRental(Long id) {
        return rentalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rental with id " + id + " not found"));
    }

    public void createRental(RentalDTO rentalDTO) {
        // Get current authenticated user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserByEmail(authentication.getName()).getId();

        Rental rental = rentalDTO.toRental();
        rental.setOwnerId(userId);
        rental.setCreatedAt(LocalDateTime.now());
        rental.setUpdatedAt(LocalDateTime.now());

        rentalRepository.save(rental);
    }

    public void updateRental(Long id, RentalDTO rentalDTO) {
        // Get current authenticated user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserByEmail(authentication.getName()).getId();

        Rental existingRental = getRental(id);

        // Check if the rental belongs to the current user
        if (!existingRental.getOwnerId().equals(userId)) {
            throw new SecurityException("You are not authorized to update this rental");
        }

        // Update fields (only if provided)
        if (rentalDTO.getName() != null && !rentalDTO.getName().isEmpty()) {
            existingRental.setName(rentalDTO.getName());
        }

        if (rentalDTO.getSurface() != null) {
            existingRental.setSurface(rentalDTO.getSurface());
        }

        if (rentalDTO.getPrice() != null) {
            existingRental.setPrice(rentalDTO.getPrice());
        }

        if (rentalDTO.getPicture() != null && !rentalDTO.getPicture().isEmpty()) {
            existingRental.setPicture(rentalDTO.getPicture());
        }

        if (rentalDTO.getDescription() != null && !rentalDTO.getDescription().isEmpty()) {
            existingRental.setDescription(rentalDTO.getDescription());
        }

        existingRental.setUpdatedAt(LocalDateTime.now());

        rentalRepository.save(existingRental);
    }
}