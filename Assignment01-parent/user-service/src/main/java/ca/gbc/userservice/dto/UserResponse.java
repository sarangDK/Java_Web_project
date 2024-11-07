package ca.gbc.userservice.dto;

import ca.gbc.userservice.model.Type;

public record UserResponse(
        Long user_id,
        String user_name,
        String user_email,
        Type type
) {

}