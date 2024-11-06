package ca.gbc.eventservice.dto;

public record EventResponse(
        String eventId,
        Long roomId,
        String eventName,
        String eventType,
        Integer expectedAttendees
) {
}
