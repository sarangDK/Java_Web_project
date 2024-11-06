package ca.gbc.roomservice.dto;

public record RoomResponse(
        Long roomId,
        String roomName,
        String roomCapacity,
        Boolean roomAvailability,
        String roomFeatures
) {
}
