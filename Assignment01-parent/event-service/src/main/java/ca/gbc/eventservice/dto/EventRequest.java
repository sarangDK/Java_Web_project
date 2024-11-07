package ca.gbc.eventservice.dto;

public record EventRequest(
        String eventId,
        String userId,
        String eventName,
        String eventType,
        Integer expectedAttendees
) {
}
