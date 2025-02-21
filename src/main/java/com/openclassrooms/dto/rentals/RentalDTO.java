package com.openclassrooms.dto.rentals;

import com.openclassrooms.models.Rental;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalDTO {
    private Long id;

    @NotEmpty(message = "Name is required")
    private String name;

    @NotNull(message = "Surface is required")
    @Positive(message = "Surface must be positive")
    private BigDecimal surface;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotEmpty(message = "Picture URL is required")
    private String picture;

    @NotEmpty(message = "Description is required")
    private String description;

    private Long ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Static factory method to convert Rental entity to DTO
    public static RentalDTO fromRental(Rental rental) {
        RentalDTO dto = new RentalDTO();
        dto.setId(rental.getId());
        dto.setName(rental.getName());
        dto.setSurface(rental.getSurface());
        dto.setPrice(rental.getPrice());
        dto.setPicture(rental.getPicture());
        dto.setDescription(rental.getDescription());
        dto.setOwnerId(rental.getOwnerId());
        dto.setCreatedAt(rental.getCreatedAt());
        dto.setUpdatedAt(rental.getUpdatedAt());
        return dto;
    }

    // Convert DTO to entity
    public Rental toRental() {
        Rental rental = new Rental();
        rental.setId(this.id);
        rental.setName(this.name);
        rental.setSurface(this.surface);
        rental.setPrice(this.price);
        rental.setPicture(this.picture);
        rental.setDescription(this.description);
        rental.setOwnerId(this.ownerId);
        rental.setCreatedAt(this.createdAt);
        rental.setUpdatedAt(this.updatedAt);
        return rental;
    }
}