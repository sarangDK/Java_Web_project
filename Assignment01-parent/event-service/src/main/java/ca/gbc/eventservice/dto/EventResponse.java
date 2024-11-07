package ca.gbc.eventservice.dto;

public record EventResponse(
        String eventId,
        String userId,
        String eventName,
        String eventType,
        Integer expectedAttendees
) {
}
