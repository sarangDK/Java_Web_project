package ca.gbc.eventservice.dto;

public record EventRequest(
        String eventId,
        String bookingId,
        String eventName,
        String eventType,
        Integer expectedAttendees
) {
}
