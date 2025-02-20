package com.openclassrooms.dto;

import com.openclassrooms.models.User;
import lombok.Data;

@Data
public class UserDetailsDTO {
    private Long id;
    private String name;
    private String email;
    private String created_at;
    private String updated_at;

    public static UserDetailsDTO fromUser(User user) {
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCreated_at(user.getCreated_at());
        dto.setUpdated_at(user.getUpdated_at());
        return dto;
    }
}