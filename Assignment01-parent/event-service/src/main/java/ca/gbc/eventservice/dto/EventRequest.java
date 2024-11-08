package ca.gbc.eventservice.dto;

public record EventRequest(
        String eventId,
        Long userId,
        String eventName,
        String eventType,
        Integer expectedAttendees
) {
}
