package ca.gbc.eventservice.dto;

public record EventResponse(
        String eventId,
        String roomId,
        String eventName,
        String eventType,
        int expectedAttendees
) {
}
