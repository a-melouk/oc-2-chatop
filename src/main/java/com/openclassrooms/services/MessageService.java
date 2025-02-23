package com.openclassrooms.services;

import com.openclassrooms.dto.MessageDTO;
import com.openclassrooms.models.Message;
import com.openclassrooms.repositories.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final RentalService rentalService;

    public MessageService(MessageRepository messageRepository, UserService userService, RentalService rentalService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.rentalService = rentalService;
    }

    public void createMessage(MessageDTO messageDTO) {
        // Verify that both user and rental exist
        userService.getUser(messageDTO.getUserId());
        rentalService.getRental(messageDTO.getRentalId());

        Message message = messageDTO.toMessage();
        messageRepository.save(message);
    }
}
