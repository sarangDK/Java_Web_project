package ca.gbc.eventservice.dto;

public record EventResponse(
        String eventId,
        String bookingId,
        String eventName,
        String eventType,
        Integer expectedAttendees
) {
}
