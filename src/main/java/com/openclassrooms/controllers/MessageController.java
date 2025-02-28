package com.openclassrooms.controllers;

import com.openclassrooms.dto.MessageDTO;
import com.openclassrooms.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@Log
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    @Operation(summary = "Create new message", description = "Creates a new message in the system", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Map<String, String>> createMessage(@Valid @RequestBody MessageDTO messageDTO) {
        messageService.createMessage(messageDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", messageDTO.getMessage());  // Return the actual message that was sent

        return ResponseEntity.ok(response);
    }
}