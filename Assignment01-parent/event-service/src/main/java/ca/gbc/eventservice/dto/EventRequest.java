package ca.gbc.eventservice.dto;

public record EventRequest(
        String eventId,
        Long roomId,
        String eventName,
        String eventType,
        Integer expectedAttendees
) {
}
