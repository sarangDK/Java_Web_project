package ca.gbc.roomservice.dto;

public record RoomResponse(
        Long room_id,
        String room_name,
        String room_capacity,
        Boolean room_availability,
        String room_features
) {
}
