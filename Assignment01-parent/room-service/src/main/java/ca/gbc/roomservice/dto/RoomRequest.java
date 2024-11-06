package ca.gbc.roomservice.dto;

public record RoomRequest(
        Long roomId,
        String roomName,
        String roomCapacity,
        Boolean roomAvailability,
        String roomFeatures
) {
}
