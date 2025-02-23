package com.openclassrooms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.openclassrooms.models.Message;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    @JsonProperty("user_id")
    @NotNull(message = "User ID is required")
    private Long userId;

    @JsonProperty("rental_id")
    @NotNull(message = "Rental ID is required")
    private Long rentalId;

    @NotNull(message = "Message content is required")
    private String message;

    public Message toMessage() {
        Message msg = new Message();
        msg.setUserId(this.userId);
        msg.setRentalId(this.rentalId);
        msg.setMessage(this.message);
        return msg;
    }
}
