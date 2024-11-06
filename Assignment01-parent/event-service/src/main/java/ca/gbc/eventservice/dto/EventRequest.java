package ca.gbc.eventservice.dto;

public record EventRequest(
        String eventId,
        String roomId,
        String eventName,
        String eventType,
        int expectedAttendees
) {
}
