package com.openclassrooms.dto.rentals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponse {
    private String message;

    public static RentalResponse success(String action) {
        return new RentalResponse("Rental successfully " + action);
    }

    public static RentalResponse error(String action) {
        return new RentalResponse("Rental error " + action);
    }
}