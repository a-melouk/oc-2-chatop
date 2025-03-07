package com.openclassrooms.controllers;

import com.openclassrooms.dto.rentals.RentalDTO;
import com.openclassrooms.dto.rentals.RentalResponse;
import com.openclassrooms.models.Rental;
import com.openclassrooms.services.RentalService;
import com.openclassrooms.services.UserService;
import com.openclassrooms.utils.FileUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rentals")
@Log
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;

    public RentalController(RentalService rentalService, UserService userService) {
        this.rentalService = rentalService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all rentals", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Map<String, List<RentalDTO>>> getAllRentals() {
        List<RentalDTO> rentals = rentalService.getAllRentals().stream().map(RentalDTO::fromRental).collect(Collectors.toList());

        Map<String, List<RentalDTO>> response = new HashMap<>();
        response.put("rentals", rentals);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get rental by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RentalDTO> getRental(@PathVariable Long id) {
        return ResponseEntity.ok(RentalDTO.fromRental(rentalService.getRental(id)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new rental", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RentalResponse> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile picture) {

        try {
            RentalDTO rentalDTO = new RentalDTO();
            rentalDTO.setName(name);
            rentalDTO.setSurface(surface);
            rentalDTO.setPrice(price);
            rentalDTO.setDescription(description);

            // Process the picture file
            String fileName = processImageFile(picture);
            if (fileName != null) {
                String pictureUrl = "http://127.0.0.1:5500/" + fileName;
                rentalDTO.setPicture(pictureUrl);
            } else {
                // Return error if picture processing failed
                return new ResponseEntity<>(RentalResponse.error("error"), HttpStatus.BAD_REQUEST);
            }

            rentalService.createRental(rentalDTO);
            return new ResponseEntity<>(RentalResponse.success("created"), HttpStatus.CREATED);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error creating rental: " + e.getMessage(), e);
            return new ResponseEntity<>(RentalResponse.success("error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing rental", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RentalResponse> updateRental(@PathVariable Long id,
                                                       @RequestParam("name") String name,
                                                       @RequestParam("surface") BigDecimal surface,
                                                       @RequestParam("price") BigDecimal price,
                                                       @RequestParam("description") String description,
                                                       @RequestParam(value = "picture", required = false) MultipartFile picture) {
        log.warning("Updating rental");
        try {
            // First, get the existing rental to preserve data if needed
            Rental existingRental = rentalService.getRental(id);

            RentalDTO rentalDTO = new RentalDTO();
            rentalDTO.setName(name);
            rentalDTO.setSurface(surface);
            rentalDTO.setPrice(price);
            rentalDTO.setDescription(description);

            // Set the existing picture URL by default
            rentalDTO.setPicture(existingRental.getPicture());

            // Process the picture file if provided
            if (picture != null && !picture.isEmpty()) {
                String fileName = processImageFile(picture);
                if (fileName != null) {
                    String pictureUrl = "http://127.0.0.1:5500/" + fileName;
                    rentalDTO.setPicture(pictureUrl);
                } else {
                    log.warning("No valid picture file was processed during update, keeping existing picture");
                }
            }

            rentalService.updateRental(id, rentalDTO);
            return ResponseEntity.ok(RentalResponse.success("updated"));
        } catch (SecurityException e) {
            log.warning("Authorization error: " + e.getMessage());
            // Return a specific authorization error with the actual message
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new RentalResponse(e.getMessage()));
        } catch (Exception e) {
            log.warning("Error updating rental: " + e.getMessage());
            // Return an error response with the correct error method
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RentalResponse.error("updating"));
        }
    }

    /**
     * Processes an image file and returns the filename
     * Supports multiple image formats and performs validation
     *
     * @param file The MultipartFile to process
     * @return The extracted filename or null if invalid
     */
    private String processImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warning("Empty file provided");
            return null;
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            log.warning("Unsupported file type: " + contentType);
            return null;
        }

        // Validate file size (e.g., 5MB limit)
        if (file.getSize() > 5 * 1024 * 1024) {
            log.warning("File size exceeds limit: " + file.getSize());
            return null;
        }

        try {
            String fileName = FileUtil.extractFileName(file);
            log.info("Successfully processed file: " + fileName);
            return fileName;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error processing file: " + e.getMessage(), e);
            return null;
        }
    }
}