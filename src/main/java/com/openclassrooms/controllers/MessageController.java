package com.openclassrooms.controllers;

import com.openclassrooms.dto.MessageDTO;
import com.openclassrooms.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")

public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    @Operation(summary = "Create a new message", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Map<String, String>> createMessage(@Valid @RequestBody MessageDTO messageDTO) {
        messageService.createMessage(messageDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", messageDTO.getMessage());  // Return the actual message that was sent

        return ResponseEntity.ok(response);
    }
}
